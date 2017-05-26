package org.wifry.fooddelivery;


import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = SpringConfig.class, loader = AnnotationConfigContextLoader.class)
public class JUnitSpringExample {

/*
    @Autowired
    private CompChequeService chequeService;
*/

//    @Autowired
//    private CargoService cargoService;

    @Test
    public void testInsertCargo() {
//        for (int i = 0; i < 5; i++) {
//            Cargo cargo = new Cargo(Estado.ACTIVO, "Cargo " + (i + 1), "Descripción Cargo " + (i + 1));
//            cargoService.save(cargo);
//        }
    }


    /*//    @Test
    public void testComprobante() {
       *//* List<String> byBeneficiario = chequeService.getAllBeneficiarios();
        assertNotNull(byBeneficiario);StrSubstitutor

        *//*

        *//*Map<String, String> replacements = new HashMap<String, String>() {{
            put("${env1}", "1");
            put("${env2}", "2");
            put("${env3}", "3");
            put("${env4}", "4");
        }};

        String line = "${env1}soj${env4}ods${env2}${env3}";
        String rx = "(\\$\\{[^}]+\\})";

        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(line);

        while (m.find()) {
            // Avoids throwing a NullPointerException in the case that you
            // Don't have a replacement defined in the map for the match
            String group = m.group(1);
            String repString = replacements.get(group);
            if (repString != null)
                m.appendReplacement(sb, repString);
        }
        m.appendTail(sb);

        System.out.println(sb.toString());*//*

        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("state", "Andhra Pradesh");
        valueMap.put("capital", "Hyderabad");

        String varPrefix = "${";
        String varSuffix = "}";
        String template = "The ${capital} capital of ${state} is ${capital}";
        System.out.println(StrSubstitutor.replace(template, valueMap, varPrefix, varSuffix));

        varPrefix = "<<";
        varSuffix = ">>";
        template = "The capital of <<state>> is <<capital>>";
        System.out.println(StrSubstitutor.replace(template, valueMap, varPrefix, varSuffix));
    }
*/
    @Test
    public void testComprobante() {
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("state", "Andhra Pradesh");
        valueMap.put("capital", "Hyderabad");

        String varPrefix = "${";
        String varSuffix = "}";
        String template = "The ${capital} capital of ${state} is ${capital}";
        System.out.println(StrSubstitutor.replace(template, valueMap, varPrefix, varSuffix));
    }
}
