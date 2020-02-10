/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
***************************************************************************/
package org.vgu.ocl2psql.roberts;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.vgu.ocl2psql.ocl.parser.Ocl2PsqlSvc;
import org.vgu.ocl2psql.ocl.parser.simple.SimpleO2PApi;

public class OCL2PSQLManualTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        File contextModel = new File(
//                "src/main/resources/context-model/alice_context_1.json");
//                "src/main/resources/context-model/alice_context.json");
            "src/main/resources/context-model/CarPerson_context-new-model.json");
        // This is for the new model
//                "src/main/resources/genSQL/uni_pof_dm.json");
//                "src/main/resources/context-model/many_to_many.json");
//                "src/main/resources/context-model/many_to_one.json");
//                "src/main/resources/context-model/one_to_one.json");

//        Ocl2PsqlSvc robertO2P = new LegacyO2PApi();
//        robertO2P.setPlainUMLContextFromFile(
//                contextModel.getAbsolutePath());
//        robertO2P.setDescriptionMode(true);
//        robertO2P.setContextualType("Car");

        Ocl2PsqlSvc simpleO2P = new SimpleO2PApi();
        simpleO2P.setDataModelFromFile(contextModel.getAbsolutePath());
//        simpleO2P.setDescriptionMode(true);
//        simpleO2P.setContextualType("self", "Person");
//        simpleO2P.setContextualType("caller", "Person");
//        simpleO2P.setContextualType("kself", "Staff");
//        simpleO2P.setContextualType("kcaller", "Staff");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String now = df.format(LocalDateTime.now());
        String resFile = "src/test/resources/bothResult_" + now + ".txt";
        try (FileWriter resWr = new FileWriter(resFile)) {

//            for (int i = 0; i < legacyCases.length; i++) {
//                resWr.append("\n\n================ ~ BEGIN ROBERT ~ ================\n\n");
//                resWr.append(legacyCases[i]);
//                resWr.append("\n\n===========================================");
//
//                resWr.append("\n================ ~ Robert ~ ================\n\n");
//                String robert = test(robertO2P, legacyCases[i]);
//                resWr.append(robert);
//
//                resWr.append("\n================ ~ //END ROBERT ~ ================\n\n");
//            }

            for (int i = 0; i < simpleCases.length; i++) {
//                resWr.append("\n\n================ ~ BEGIN SIMPLE ~ ================\n\n");
                resWr.append(simpleCases[i]);
//                resWr.append("\n\n===========================================");
                resWr.append("\n");
//                resWr.append("\n\n================ ~ Simple ~ ================\n\n");
                String simple = test(simpleO2P, simpleCases[i]);
                resWr.append(simple);

//                resWr.append("\n================ ~ //END SIMPLE ~ ================\n\n");
            }

            resWr.flush();
        }
    }

    private static String test(Ocl2PsqlSvc o2p, String oclExp) {
//        System.out.println("\n=====\n" + oclExp + "\n=====\n");
        String finalStatementWithDescription = o2p.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        System.out.println();
        return finalStatementWithDescription;
    }

    private static String[] simpleCases = {
        "2",
        "'Peter'",
        "true",

        "2=3",
        "'no-name' = 'Peter'",
        "true or true",

        "Car.allInstances()",

        "Car.allInstances()->size()",
        "Car.allInstances()->size() = 1",

        "Car.allInstances()->collect(c|5)",
        "Car.allInstances()->collect(c|c)",
        "Car.allInstances()->collect(c|5 = 1)",

        "Car.allInstances()->collect(c|c.color)",
        "Car.allInstances()->collect(c|c.color = 'black')",

        "Car.allInstances()->collect(c|c.owners->size())",
        "Car.allInstances()->collect(c|c.owners->size() = 0)",

        "Car.allInstances()->exists(c|true)",
        "Car.allInstances()->exists(c|false)",
        "Car.allInstances()->exists(c|c.color = 'black')",
        "Car.allInstances()->exists(c|c.owners->size() = 1)",

        "Car.allInstances()->forAll(c|false)",
        "Car.allInstances()->forAll(c|true)",
        "Car.allInstances()->forAll(c|c.color='black')",
        "Car.allInstances()->forAll(c|c.owners->size() = 1)",

        "Car.allInstances()->forAll(c|c.owners->exists(p|p.name = 'Peter'))",
        "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'Peter'))",
        "Car.allInstances()->exists(c|c.owners->exists(p|p.name = 'Peter'))",
        "Car.allInstances()->forAll(c|c.owners->forAll(p|p.name = 'Peter'))"
    };

}
