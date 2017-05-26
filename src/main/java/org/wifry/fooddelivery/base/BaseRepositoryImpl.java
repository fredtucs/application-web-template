package org.wifry.fooddelivery.base;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;


public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager em;
    private final Class<T> entityClass;

    BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        Assert.notNull(entityInformation);
        Assert.notNull(entityManager);
        this.em = entityManager;
        entityClass = entityInformation.getJavaType();
    }

    @Override
    public void updateState(T entity) {
        StringBuilder hql = new StringBuilder("UPDATE ");
        hql.append(entity.getClass().getName());
        hql.append(" SET estado = ?0 WHERE ");
        Metadata metadata = HibernateMetadataUtil.getInstanceForEntityManagerFactory(em.getEntityManagerFactory()).get(entityClass);
        hql.append(metadata.getIdProperty());
        hql.append(" = ?1");
        Query query = em.createQuery(hql.toString());
        query.setParameter(0, metadata.getPropertyValue(entity, "estado"));
        query.setParameter(1, metadata.getIdValue(entity));
        query.executeUpdate();
    }

}
