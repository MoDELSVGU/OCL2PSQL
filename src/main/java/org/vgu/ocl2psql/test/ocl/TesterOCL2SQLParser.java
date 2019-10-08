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
package org.vgu.ocl2psql.test.ocl;

import java.io.File;

import org.vgu.ocl2psql.main.OCL2PSQL;
import org.vgu.ocl2psql.ocl.exception.OclParseException;


public class TesterOCL2SQLParser {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
            throws Exception {

        OCL2PSQL ocl2psql = new OCL2PSQL();
        File contextModel = new File("./src/main/resources/context-model/old-CarPerson_context.json");
        ocl2psql.setPlainUMLContextFromFile(contextModel.getAbsolutePath());
        ocl2psql.setDescriptionMode(true);
        
        test(ocl2psql, "Car::allInstances()->isUnique()");
    }

    private static void test(OCL2PSQL ocl2psql, String oclExp) throws OclParseException {
//        System.out.println(oclExp);
        String finalStatementWithDescription = ocl2psql.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        System.out.println();
    }
}
