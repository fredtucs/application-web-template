package org.wifry.fooddelivery.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class ObjectUtils {

    public static Boolean isEmptyAll(Object... obj) {
        boolean band = true;
        if (obj == null)
            return true;
        for (Object object : obj) {
            if (!isEmpty(object)) {
                band = false;
                break;
            }
        }
        return band;
    }

    @SuppressWarnings("rawtypes")
    public static Boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof String)
            return ((String) obj).trim().length() == 0;
        if (obj instanceof Collection)
            return ((Collection) obj).size() == 0;
        if (obj instanceof Map)
            return ((Map) obj).size() == 0;
        if (obj instanceof Serializable)
            return obj == null;

        int count = 0;
        if (obj instanceof Iterator) {
            Iterator iter = (Iterator) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count == 0;
        }
        if (obj instanceof Enumeration) {
            Enumeration enum_ = (Enumeration) obj;
            count = 0;
            while (enum_.hasMoreElements()) {
                count++;
                enum_.nextElement();
            }
            return count == 0;
        }
        try {
            count = Array.getLength(obj);
            return count == 0;
        } catch (IllegalArgumentException ex) {
            return true;
        }

    }

    public static boolean equals(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

}
