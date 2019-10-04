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

import java.io.InputStream;
import java.util.Properties;

import org.vgu.ocl2psql.main.OCL2PSQL;
import org.vgu.ocl2psql.ocl.exception.OclParseException;


public class TesterOCL2SQLParser {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
            throws Exception {
        String resourceName = "config.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)){
            props.load(resourceStream);

            OCL2PSQL ocl2psql = new OCL2PSQL();
            ocl2psql.setPlainUMLContextFromFile(props.getProperty("cardb.filePath"));
            ocl2psql.setDescriptionMode(true);
            
//            test(ocl2psql, "self");
//            test(ocl2psql, "self = caller");
//            test(ocl2psql, "self.Person:name");
//            test(ocl2psql, "self.Person:name = 'Hoang'");
//            test(ocl2psql, "self.Person:ownedCars");
//            test(ocl2psql, "self.Person:ownedCars->size()");
//            test(ocl2psql, "self.Person:ownedCars->exists(c|c.Car:color = 'black')");
//            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->exists(p|p = self))");
//            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->exists(p|p.Person:ownedCars->size() < self.Person:ownedCars->size()))");
        
            test(ocl2psql, "true");
            test(ocl2psql, "1");
            test(ocl2psql, "'Hoang'");
            test(ocl2psql, "Car::allInstances()");
            test(ocl2psql, "self.Car:color");
            test(ocl2psql, "self.Car:owners");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
        }
    }

    private static void test(OCL2PSQL ocl2psql, String oclExp) throws OclParseException {
        System.out.println(oclExp);
        String finalStatementWithDescription = ocl2psql.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        System.out.println();
    }
}
