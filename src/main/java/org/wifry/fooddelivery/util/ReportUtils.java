package org.wifry.fooddelivery.util;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class ReportUtils {

    private static final Map<Locale, Map<String, Integer>> MONTHS_CACHE = new HashMap<Locale, Map<String, Integer>>(3);
    private static volatile ReportUtils instance = null;
    private static Locale locale;
    private final String[] UNIDADES = {"", "un ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ",
            "nueve "};
    private final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ",
            "diecisiete ", "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ", "cincuenta ", "sesenta ",
            "setenta ", "ochenta ", "noventa "};
    private final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ",
            "seiscientos ", "setecientos ", "ochocientos ", "novecientos "};
    private BigDecimal totalPageDebe;
    private BigDecimal totalPageHaber;
    private BigDecimal totalPageSaldo;

    public ReportUtils() {
    }

    public static ReportUtils getInstance(Locale locale) {
        ReportUtils.locale = locale;
        if (instance == null) {
            instance = new ReportUtils();
        }
        return instance;
    }

    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }

    public static boolean isNoZero(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof String) {
            for (int i = 0; i < obj.toString().length(); i++) {
                char c = obj.toString().charAt(i);
                if ((c > 48 && c <= 57)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Integer)
            return Long.valueOf(obj.toString()) > 0L;
        return false;

    }

    public static String listAlphabet(List<String> referencias) {
        String[] alf = {"a", "b", "c", "d", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};
        StringBuilder lista = new StringBuilder();
        int c = 0;
        if (referencias != null)
            for (String d : referencias) {
                lista.append(alf[c++]).append(". ");
                lista.append(d);
                lista.append("<br/> ");
            }
        return lista.toString();
    }

    public static String formatNumber(Object value) {
        if (ObjectUtils.isEmpty(value))
            return "";
        if (locale == null)
            locale = Locale.getDefault();

        DecimalFormat decimalFormat = Utils.createDecimalFormat(locale);
        return decimalFormat.format(value);
    }

    public static String getLastDate(int month, int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        Date date = calendar.getTime();
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        return DATE_FORMAT.format(date);
    }

    public String replaceVars(String text, Map<String, Object> valueMap) {
        try {
            String varPrefix = "${";
            String varSuffix = "}";
            return StrSubstitutor.replace(text, valueMap, varPrefix, varSuffix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getMonth(Integer monthNumber) {
        for (Entry<String, Integer> entry : getMonths().entrySet()) {
            if (monthNumber.equals(entry.getValue())) {
                return entry.getKey().toUpperCase();
            }
        }
        return null;
    }

    public Map<String, Integer> getMonths() {
        Map<String, Integer> months = MONTHS_CACHE.get(locale);
        if (months == null) {
            months = mapMonths(DateFormatSymbols.getInstance(locale).getMonths());
            MONTHS_CACHE.put(locale, months);
        }
        return months;
    }

    private Map<String, Integer> mapMonths(String[] months) {
        Map<String, Integer> mapping = new LinkedHashMap<String, Integer>();
        for (String month : months) {
            if (!month.isEmpty()) {
                mapping.put(month, mapping.size() + 1);
            }
        }
        return mapping;
    }

    public String nroLetra(String numero) {
        String literal = "";
        String parte_decimal;

        if (numero == null || numero.isEmpty())
            return null;

        numero = new BigDecimal(numero).setScale(2, BigDecimal.ROUND_CEILING).toString();
        // si el numero utiliza (.) en lugar de (,) -> se reemplaza
        numero = numero.replace(".", ",");
        // si el numero no tiene parte decimal, se le agrega ,00
        if (numero.indexOf(",") == -1) {
            numero = numero + ",00";
        }
        // se valida formato de entrada -> 0,00 y 999 999 999,00
        if (Pattern.matches("\\d{1,9},\\d{1,2}", numero)) {
            // se divide el numero 0000000,00 -> entero y decimal
            String Num[] = numero.split(",");
            // de da formato al numero decimal
            parte_decimal = Num[1] + "/100 NUEVOS SOLES";
            // se convierte el numero a literal
            if (Integer.parseInt(Num[0]) == 0) {// si el valor es cero
                literal = "cero ";
            } else if (Integer.parseInt(Num[0]) > 999999) {// si es millon
                literal = getMillones(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 999) {// si es miles
                literal = getMiles(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 99) {// si es centena
                literal = getCentenas(Num[0]);
            } else if (Integer.parseInt(Num[0]) > 9) {// si es decena
                literal = getDecenas(Num[0]);
            } else {// sino unidades -> 9
                literal = getUnidades(Num[0]);
            }

            return (literal + " con " + parte_decimal).toUpperCase();
        } else {// error, no se puede convertir
            return literal = null;
        }
    }

	/* funciones para convertir los numeros a literales */

    private String getUnidades(String numero) {// 1 - 9
        // si tuviera algun 0 antes se lo quita -> 09 = 9 o 009=9
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private String getDecenas(String num) {// 99
        int n = Integer.parseInt(num);
        if (n < 10) {// para casos como -> 01 - 09
            return getUnidades(num);
        } else if (n > 19) {// para 20...99
            String u = getUnidades(num);
            if (u.equals("")) { // para 20,30,40,50,60,70,80,90
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
            } else {
                return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8] + "y " + u;
            }
        } else {// numeros entre 11 y 19
            return DECENAS[n - 10];
        }
    }

    private String getCentenas(String num) {// 999 o 099
        if (Integer.parseInt(num) > 99) {// es centena
            if (Integer.parseInt(num) == 100) {// caso especial
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecenas(num.substring(1));
            }
        } else {// por Ej. 099
            // se quita el 0 antes de convertir a decenas
            return getDecenas(Integer.parseInt(num) + "");
        }
    }

    private String getMiles(String numero) {// 999 999
        // obtiene las centenas
        String c = numero.substring(numero.length() - 3);
        // obtiene los miles
        String m = numero.substring(0, numero.length() - 3);
        String n = "";
        // se comprueba que miles tenga valor entero
        if (Integer.parseInt(m) > 0) {
            n = getCentenas(m);
            return n + "mil " + getCentenas(c);
        } else {
            return "" + getCentenas(c);
        }

    }

    private String getMillones(String numero) { // 000 000 000
        // se obtiene los miles
        String miles = numero.substring(numero.length() - 6);
        // se obtiene los millones
        String millon = numero.substring(0, numero.length() - 6);
        String n = "";
        if (Long.valueOf(millon) > 1L) {
            n = getCentenas(millon) + "millones ";
        } else {
            n = getUnidades(millon) + "millon ";
        }
        return n + getMiles(miles);
    }

    public String leftPad(Integer input, Integer length, String padding) {
        return org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(input), length, padding);
    }

    public BigDecimal getTotalPageDebe() {
        return totalPageDebe;
    }

    public void setTotalPageDebe(BigDecimal totalPageDebe) {
        this.totalPageDebe = totalPageDebe;
    }

    public BigDecimal getTotalPageHaber() {
        return totalPageHaber;
    }

    public void setTotalPageHaber(BigDecimal totalPageHaber) {
        this.totalPageHaber = totalPageHaber;
    }

    public BigDecimal getTotalPageSaldo() {
        return totalPageSaldo;
    }

    public void setTotalPageSaldo(BigDecimal totalPageSaldo) {
        this.totalPageSaldo = totalPageSaldo;
    }

}
