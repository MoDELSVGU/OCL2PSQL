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
                "src/main/resources/context-model/company-dm.json");

        Ocl2PsqlSvc simpleO2P = new SimpleO2PApi();
        simpleO2P.setDataModelFromFile(contextModel.getAbsolutePath());
        simpleO2P.setDescriptionMode(false);
        simpleO2P.setContextualType("self", "Employee");
        simpleO2P.setContextualType("caller", "Employee");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        String now = df.format(LocalDateTime.now());
        String resFile = "src/test/resources/bothResult_" + now + ".txt";
        try (FileWriter resWr = new FileWriter(resFile)) {

            for (int i = 0; i < simpleCases.length; i++) {
                resWr.append(simpleCases[i]);
                resWr.append("\n");
                String simple = test(simpleO2P, simpleCases[i]);
                resWr.append(simple);
            }

            resWr.flush();
        }
    }

    private static String test(Ocl2PsqlSvc o2p, String oclExp) {
        String finalStatementWithDescription = o2p.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        return finalStatementWithDescription;
    }

    private static String[] simpleCases = {
        "true",
        "self = caller",
        "self.supervisor->exists(s|s=caller)",
        "self = caller or self.supervisor->exists(s|s=caller)"
    };

}
