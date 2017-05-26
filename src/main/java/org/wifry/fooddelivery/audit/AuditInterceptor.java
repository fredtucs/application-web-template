package org.wifry.fooddelivery.audit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.*;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.CompositeCustomType;
import org.hibernate.type.ManyToOneType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.wifry.fooddelivery.model.AuditLog;
import org.wifry.fooddelivery.model.User;
import org.wifry.fooddelivery.security.SpringSecurityUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

@Component
public class AuditInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = -653495231329115390L;

    private static final String COLUMN_NAME = "columnName";
    private static final String TABLE_NAME = "tableName";

    /**
     * Cache for getColumnTableName.
     */
    private static final Map<String, Map<String, String>> COLUMN_CACHE = new HashMap<String, Map<String, String>>();

    @PersistenceContext
    private EntityManager em;

    /**
     * Set to contain insert update log records.
     */
    private transient ThreadLocal<Set<AuditLog>> audits = new ThreadLocal<Set<AuditLog>>();

    private static final Logger LOG = LoggerFactory.getLogger(AuditInterceptor.class);


    private Session getSession() {
        return em.unwrap(Session.class);
    }

    private SessionFactory getSessionFactory() {
        return getSession().getSessionFactory();
    }


    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state,
                          String[] propertyNames, Type[] types) {
        if (isAuditableEntity(entity)) {
            auditChangesIfNeeded(entity, id, state, new Object[propertyNames.length], propertyNames, types, AuditType.INSERT);
        }
        return audit(state, propertyNames);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id,
                                Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        if (isAuditableEntity(entity)) {
            auditChangesIfNeeded(entity, id, currentState, previousState, propertyNames, types, AuditType.UPDATE);
        }
        return audit(currentState, propertyNames);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state,
                         String[] propertyNames, Type[] types) {
        if (isAuditableEntity(entity)) {
            auditChangesIfNeeded(entity, id, new Object[propertyNames.length], state, propertyNames, types, AuditType.DELETE);
        }
        audit(state, propertyNames);
    }


    private boolean audit(Object[] currentState, String[] propertyNames) {
        boolean changed = false;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        for (int i = 0; i < propertyNames.length; i++) {

            if ("createdBy".equals(propertyNames[i])) {
                Object currentBy = currentState[i];
                if (currentBy == null) {
                    currentState[i] = SpringSecurityUtils.getCurrentUserName();
                    changed = true;
                }
            }

            if ("createdDate".equals(propertyNames[i])) {
                Object currentDate = currentState[i];
                if (currentDate == null) {
                    currentState[i] = timestamp;
                    changed = true;
                }
            }

            if ("lastModifiedBy".equals(propertyNames[i])) {
                currentState[i] = SpringSecurityUtils.getCurrentUserName();
                changed = true;
            }

            if ("lastModifiedDate".equals(propertyNames[i])) {
                currentState[i] = timestamp;
                changed = true;
            }
        }
        return changed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void postFlush(Iterator entities) {
        if (audits.get() != null && !audits.get().isEmpty()) {
            Session session = getSession();
            try {
                final HashSet<AuditLog> d = new HashSet<AuditLog>(audits.get());
                Iterator<AuditLog> it = d.iterator();
                while (it.hasNext()) {
                    AuditLog audit = it.next();
                    it.remove();
                    session.save(audit);
                }

            } catch (HibernateException e) {
                LOG.error(e.toString());
                throw new CallbackException(e);
            } finally {
                audits.get().clear();
                session.flush();
            }
        }
    }

    private void auditChangesIfNeeded(Object auditableObj, Serializable id, Object[] newValues, Object[] oldValues, String[] properties,
                                      Type[] types, AuditType eventToLog) {

        // first make sure we have old values around. The passed in set will be
        // null when an object is being updated
        // that was detached from the session
        Object[] nonNullOldValues = getOldValues(oldValues, properties, auditableObj, id);

        if (audits.get() == null) {
            audits.set(new HashSet<AuditLog>());
        }

        AuditLog record = null;
        JSONArray list = new JSONArray();
        try {
            for (int i = 0; i < properties.length; i++) {
                String property = properties[i];
                Object oldValue = nonNullOldValues[i];
                Object newValue = newValues[i];
                if (needsAuditing(auditableObj, types[i], newValue, oldValue, property)) {
                    Class<?> clazz = getPersistentClass(auditableObj);
                    Map<String, String> tabColMA = getColumnTableName(clazz.getName(), property);
                    if (record == null) {
                        String entityName = getPersistentClass(auditableObj).getName();
                        record = getAuditTrail(auditableObj, (Long) id, entityName, tabColMA.get(TABLE_NAME), eventToLog);
                        audits.get().add(record);
                    }
                    String colName = tabColMA.get(COLUMN_NAME);
                    if (types[i] instanceof CompositeCustomType) {
                        oldValue = getCompositeValue((CompositeCustomType) types[i], oldValue);
                        newValue = getCompositeValue((CompositeCustomType) types[i], newValue);
                    } else if (types[i] instanceof ManyToOneType) {
                        oldValue = getObjectIdValue(oldValue);
                        newValue = getObjectIdValue(newValue);
                    } else if (types[i] instanceof OneToOneType) {
                        oldValue = getObjectIdValue(oldValue);
                        newValue = getObjectIdValue(newValue);
                    }
                    list.put(getJson(property, colName, oldValue, newValue));
                }
            }
            if (record != null) {
                record.setDetailLog(list.toString());
            }
        } catch (Exception e) {
            LOG.error("Unable to read the value of a property.", e);
        }

    }

    private LinkedHashMap<String, Object> getJson(String property, String colName, Object oldValue, Object newValue) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("property", property);
        map.put("columname", colName);
        map.put("oldvalue", String.valueOf(oldValue));
        map.put("newvalue", String.valueOf(newValue));
        return map;
    }

    /**
     * Gets the id of the persisted object
     *
     * @param input the object to get the id from
     * @return object Id
     */
    private Serializable getObjectIdValue(Object input) {
        if (input == null) {
            return null;
        }
        Class<?> objectClass = input.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        Serializable persistedObjectId = null;
        for (int ii = 0; ii < fields.length; ii++) {
            if (fields[ii].isAnnotationPresent(Id.class)) {
                try {
                    Object valueId = PropertyUtils.getProperty(input, fields[ii].getName());
                    persistedObjectId = (Serializable) valueId;
                    break;
                } catch (Exception e) {
                    LOG.warn("Audit Log Failed - Could not get persisted object id: " + e.getMessage());
                }
            }
        }
        return persistedObjectId;
    }

    /**
     * @param entity     the entity being processed.
     * @param key        the id of the entity, may not be available untill detail
     *                   processing.
     * @param type       action type to audit.
     * @param entityName the entities class name.
     * @param tableName  the table the entity is mapped to.
     * @return an audit log record.
     */
    public AuditLog getAuditTrail(Object entity, Serializable key, String entityName, String tableName, AuditType type) {
        Authentication authentication = SpringSecurityUtils.getAuthentication();
        User aut = SpringSecurityUtils.getUserDetails();
        assert authentication != null;
        WebAuthenticationDetails wad = (WebAuthenticationDetails) authentication.getDetails();
        return new AuditLog((Long) key, entityName, tableName, getHasCode(entity, type), aut.getIdUser(), aut.getUsername(), new Date(), type,
                wad.getRemoteAddress());
    }

    /**
     * . Returns the Actual class name from the CGILIB Proxy classname of the
     * entity
     *
     * @param obj
     * @return
     */
    private Class<?> getPersistentClass(Object obj) {
        Class<?> clazz = obj.getClass();
        while (getSessionFactory().getClassMetadata(clazz.getName()) == null) {
            if (clazz.getSuperclass() == null) {
                return null;
            }
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    private Object getCompositeValue(CompositeCustomType type, Object input) {
        if (input == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < type.getPropertyNames().length; i++) {
                jsonObject.put(type.getPropertyNames()[i], type.getPropertyValue(input, i));
            }
        } catch (HibernateException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private Object[] getOldValues(Object[] oldValues, String[] properties, Object auditableObj, Serializable id) {
        Object[] nonNullOldValues = oldValues;

        if (nonNullOldValues == null) {
            try {
                nonNullOldValues = retrieveOldValues(getSession(), id, properties, getPersistentClass(auditableObj));
            } finally {

            }
        }
        return nonNullOldValues;
    }

    @SuppressWarnings("deprecation")
    private boolean needsAuditing(Object auditableObj, Type type, Object newValue, Object oldValue, String property) {
        if (type.isCollectionType()) {
            return collectionNeedsAuditing(auditableObj, newValue, oldValue, property);
        }
        if (type.isEntityType()) {
            if (newValue != null && !isAuditableEntity(newValue)) {
                return false;
            }
            if (oldValue != null && !isAuditableEntity(oldValue)) {
                return false;
            }
        }

        return !ObjectUtils.equals(newValue, oldValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
        if (!(collection instanceof PersistentCollection)) {
            return;
        }

        PersistentCollection pc = (PersistentCollection) collection;
        Object owner = pc.getOwner();

        if (!isAuditableEntity(owner)) {
            return;
        }

        if (audits.get() == null) {
            audits.set(new HashSet<AuditLog>());
        }

        String role = pc.getRole();

        Serializable oldSerial = pc.getStoredSnapshot();
        Object oldValue = null;
        if (oldSerial != null && pc instanceof PersistentSet) {
            // PersistentSet seems to build a strange map where the key is also
            // the value.
            oldValue = ((Map<?, ?>) oldSerial).keySet();
        } else {
            oldValue = oldSerial;
        }
        Object newValue = pc.getValue();

        int idx = role.lastIndexOf('.');
        String className = role.substring(0, idx);
        String property = role.substring(idx + 1);
        JSONArray list = new JSONArray();
        if (collectionNeedsAuditing(owner, newValue, oldValue, property)) {
            Map<String, String> tabColMA = getColumnTableName(className, property);
            AuditLog record = getAuditTrail(owner, key, className, tabColMA.get(TABLE_NAME), AuditType.UPDATE);
            audits.get().add(record);
            list.put(list.put(getJson(property, tabColMA.get(COLUMN_NAME), oldValue, newValue)));
            record.setDetailLog(list.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterTransactionCompletion(Transaction tx) {
        if (audits.get() != null) {
            audits.get().clear();
        }
    }

    private boolean collectionNeedsAuditing(Object auditableObj, Object newValue, Object oldValue, String property) {

        try {
            Method getter = auditableObj.getClass().getMethod("get" + StringUtils.capitalize(property));
            if (getter.getAnnotation(MapKey.class) != null || getter.getAnnotation(MapKeyJoinColumns.class) != null) {
                // this is some sort of map
                Map<?, ?> oldMap = (Map<?, ?>) oldValue;
                Map<?, ?> newMap = (Map<?, ?>) newValue;
                oldMap = oldMap == null ? Collections.emptyMap() : oldMap;
                newMap = newMap == null ? Collections.emptyMap() : newMap;
                return !equalsMap(oldMap, newMap);
            } else if (getter.getAnnotation(JoinTable.class) != null || getter.getAnnotation(OneToMany.class) != null) {
                Collection<?> oldSet = (Collection<?>) oldValue;
                Collection<?> newSet = (Collection<?>) newValue;
                return !CollectionUtils.isEqualCollection(oldSet == null ? Collections.emptySet() : oldSet, newSet == null ? Collections.emptySet()
                        : newSet);
            }
        } catch (SecurityException e) {
            LOG.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieve the old values.
     *
     * @param session    the session
     * @param id         the id of the object
     * @param properties the array of property names that should be retrieved and
     *                   placed in the resulting array.
     * @param theClass   the class of the object to retrieve the data from.
     * @return the array of values for the properties.
     */
    protected Object[] retrieveOldValues(Session session, Serializable id, String[] properties, Class<?> theClass) {
        Object[] nonNullOldValues = new Object[properties.length];

        Object oldObject = session.get(theClass, id);
        if (oldObject != null) {
            for (int i = 0; i < properties.length; i++) {
                try {
                    nonNullOldValues[i] = PropertyUtils.getProperty(oldObject, properties[i]);
                    getLazyLoadedObjects(session, nonNullOldValues[i]);
                } catch (Exception e) {
                    LOG.error("Unable to read the old value of a property while logging.", e);
                    nonNullOldValues[i] = null;
                }
            }
        }

        return nonNullOldValues;
    }

    /**
     * in case this is a collection which is lazy loaded by default we want to
     * make sure that we retrieve the collection items before the session is
     * closed.
     *
     * @param session          the session to load the object
     * @param lazyLoadedObject the lazily loaded object
     */
    protected void getLazyLoadedObjects(Session session, Object lazyLoadedObject) {
        if (lazyLoadedObject instanceof Collection) {
            for (Object item : (Collection<?>) lazyLoadedObject) {
                if (item instanceof PersistentClass) {
                    session.refresh(item);
                    break;
                }
            }
        }
    }

    /**
     * Retrieves the table name and the column name for the given class and
     * property.
     *
     * @param className
     * @param fieldName
     * @return Map
     */
    private synchronized Map<String, String> getColumnTableName(String className, String fieldName) {
        String hashkey = className + ";" + fieldName;
        Map<String, String> retMap = COLUMN_CACHE.get(hashkey);
        if (retMap != null) {
            return retMap;
        }

        retMap = new HashMap<String, String>();
        COLUMN_CACHE.put(hashkey, retMap);
        ClassMetadata classMetadata = getSessionFactory().getClassMetadata(className);
        AbstractEntityPersister aep = ((AbstractEntityPersister) classMetadata);
        String tableName = aep.getTableName();
        String columnName = null;
        String[] columns = aep.getPropertyColumnNames(fieldName);
        for (int columnIndex = 0; columnIndex != columns.length; columnIndex++) {
            columnName = columns[columnIndex];
        }

        if (columnName == null) {
            columnName = fieldName;
        }
        retMap.put(TABLE_NAME, tableName);
        retMap.put(COLUMN_NAME, columnName);
        return retMap;
    }


    /**
     * does not care about order.
     */
    @SuppressWarnings("deprecation")
    private static boolean equalsMap(Map<?, ?> a, Map<?, ?> b) {
        if (ObjectUtils.equals(a, b)) {
            return true;
        }
        if (a == null || b == null || a.size() != b.size()) {
            return false;
        }
        for (Map.Entry<?, ?> e : a.entrySet()) {
            // some maps may allow null as values
            if (!b.containsKey(e.getKey())) {
                return false;
            }
            Object vb = b.get(e.getKey());
            if (!ObjectUtils.equals(e.getValue(), vb)) {
                return false;
            }
        }
        return true;
    }

    private int getHasCode(Object entity, AuditType type) {
        return new HashCodeBuilder().append(entity).appendSuper(type.hashCode()).toHashCode();
    }

    private boolean isAuditableEntity(Object entity) {
        return entity.getClass().isAnnotationPresent(Auditable.class);
    }


}