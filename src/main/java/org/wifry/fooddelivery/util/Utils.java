package org.wifry.fooddelivery.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Utils {

    public static final ResourceBundle RESOURCEBUNDLE = ResourceBundle.getBundle("fooddelivery");
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    static Logger logger = Logger.getLogger(Utils.class);

    public Utils() {
    }

    public static String removeTags(String htmlText) {
        return htmlText.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "").replaceAll("&nbsp;", "").trim();
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String listAlphabet(List<String> referencias) {
        StringBuilder lista = new StringBuilder();
        int c = 97;
        if (referencias != null) {
            for (int i = 0; i < referencias.size(); i++) {
                String d = referencias.get(i);
                lista.append((char) c++).append(". ").append(d);
                if (i < referencias.size() - 1)
                    lista.append(" <br/> ");
            }
        }
        return lista.toString();
    }

    /**
     * @param text Texto a buscar
     * @return variables que tengas la siguiente forma ${var}
     * */
    public static List<String> findVariablesString(String text) {
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher m = pattern.matcher(text);
        List<String> ml = new ArrayList<>();
        while (m.find()) {
            ml.add(m.group().replace("${", "").replace("}", ""));
        }
        return ml;
    }

    public static String listAlphabet(String... referencias) {
        return listAlphabet(Arrays.asList(referencias));
    }

    public static Boolean isSemestreI() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return (month < 8);
    }

    public static Boolean isActive(Integer value) {
        return value != null && value.equals(1);
    }

    public static String camelHump(String str) {
        if (str != null && (str = str.trim()).length() > 0) {
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    public static String valueSplit(String value, Integer index) {
        if (value.contains(":") && index != null && index >= 0) {
            String[] values = value.split(":");
            return values[index];
        } else {
            return value;
        }
    }

    public static boolean isNegativeOrZero(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof BigDecimal) {
            BigDecimal numero = (BigDecimal) obj;
            if (numero.compareTo(BigDecimal.ZERO) == -1) {
                return true;
            }
        } else if (obj instanceof Integer) {
            Integer numero = (Integer) obj;
            if (numero.compareTo(0) == -1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        int i = 0;
        if (string.charAt(0) == '-') {
            if (string.length() > 1) {
                i++;
            } else {
                return false;
            }
        }
        for (; i < string.length(); i++) {
            if (string.charAt(i) == '.' || string.charAt(i) == ',') {
                continue;
            }
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Integer sumArray(Integer[] values) {
        Integer total = 0;
        if (values != null)
            for (Integer val : values) {
                if (val != null && val != 0) {
                    total += val;
                }
            }
        if (total == 0)
            return null;
        return total;
    }

    public static String getShortMonth(int month) {
       /*Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());*/
        String[] mes = {"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"};
        return mes[month - 1];
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * @param value
     * @return value BigDecimal scaled
     */
    public static BigDecimal valueScale(Object value) {
        if (value == null)
            return null;
        return new BigDecimal(value.toString()).setScale(SCALE, ROUNDING_MODE).stripTrailingZeros();
    }

    public static String convertFieldToAccessor(String filed) {
        return "get" + camelHump(filed);
    }

    public static Date getLastDateOfMonth(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month, Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMapBean(List<V> list, String keyField) {
        try {
            String accessor = convertFieldToAccessor(keyField);
            Map<K, V> map = new HashMap<K, V>();
            for (V obj : list) {
                Method method = obj.getClass().getDeclaredMethod(accessor);
                K key = (K) method.invoke(obj);
                map.put(key, obj);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <V> Object invokeMethod(String fieldAccessor, V classType) {
        try {
            final Method method = classType.getClass().getDeclaredMethod(fieldAccessor);
            return method.invoke(classType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <V> Map<String, Object> toMap(List<V> list, String keyField) {
        return toMap(list, keyField, null);
    }

    public static <V> Map<String, Object> toMap(List<V> list, String valueField, String keyField) {
        try {
            String keyAccessor = convertFieldToAccessor(keyField);
            String valueAccessor = null;
            if (valueField != null)
                valueAccessor = convertFieldToAccessor(valueField);
            Map<String, Object> map = new HashMap<String, Object>();
            for (V obj : list) {
                String key = (String) invokeMethod(keyAccessor, obj);
                Object value = (valueField != null) ? (Object) invokeMethod(valueAccessor, obj) : obj;
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <V> Map<String, Object> toMaps(List<V> list, String valueField, String... keyFields) {

        try {
            if (list == null || keyFields == null)
                return null;

            String[] keyAccessors = new String[keyFields.length];
            for (int i = 0; i < keyFields.length; i++) {
                keyAccessors[i] = convertFieldToAccessor(keyFields[i]);
            }
            String valueAccessor = null;
            if (valueField != null)
                valueAccessor = convertFieldToAccessor(valueField);
            Map<String, Object> map = new HashMap<String, Object>();
            for (V obj : list) {
                StringBuilder key = new StringBuilder();
                for (int i = 0; i < keyAccessors.length; i++) {
                    Object objkey = invokeMethod(keyAccessors[i], obj);
                    if (objkey == null)
                        continue;
                    if (i > 0 && i < keyAccessors.length - 1)
                        key.append(" - ");
                    key.append((String) objkey);
                }
                Object value = (valueField != null) ? (Object) invokeMethod(valueAccessor, obj) : obj;
                map.put(key.toString(), value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static DecimalFormatSymbols createDecimalFormatSymbol(Locale locale) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        if (locale.getCountry().equals("PE") && locale.getLanguage().equals("es")) {
            symbols.setDecimalSeparator('.');
            symbols.setGroupingSeparator(',');
        }

        return symbols;
    }

    public static DecimalFormat createDecimalFormat(Locale locale) {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", createDecimalFormatSymbol(locale));
        decimalFormat.setRoundingMode(ROUNDING_MODE);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setGroupingUsed(true);
        return decimalFormat;
    }

    public static DecimalFormat createDecimalFormatNumber(Locale locale) {

        DecimalFormat decimalFormat = new DecimalFormat("#,###", createDecimalFormatSymbol(locale));
        decimalFormat.setRoundingMode(ROUNDING_MODE);
        decimalFormat.setDecimalSeparatorAlwaysShown(false);
        decimalFormat.setGroupingUsed(true);

        return decimalFormat;
    }

    /**
     * Returns an object containing the value of any field of an object instance (even private).
     * @param classInstance An Object instance.
     * @param fieldName The name of a field in the class instantiated by classInstance
     * @return An Object containing the field value.
     * @throws SecurityException .
     * @throws NoSuchFieldException .
     * @throws ClassNotFoundException .
     * @throws IllegalArgumentException .
     * @throws IllegalAccessException .
     */
    public static Object getInstanceValue(final Object classInstance, final String fieldName) throws SecurityException, NoSuchFieldException,
            ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

        // Get the private field
        final Field field = classInstance.getClass().getDeclaredField(fieldName);
        // Allow modification on the field
        field.setAccessible(true);
        // Return the Obect corresponding to the field
        return field.get(classInstance);

    }

}
