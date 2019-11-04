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
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.main.OCL2PSQL;
import org.vgu.ocl2psql.ocl.parser.O2P;
import org.vgu.ocl2psql.ocl.parser.simple.SimpleO2PApi;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.parser.LegacyO2PApi;


public class OCL2PSQLManualTest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
            throws Exception {

        File contextModel = new File(
//                "src/main/resources/context-model/alice_context.json");
                "src/main/resources/context-model/CarPerson_context.json");

        O2P robertO2P = new LegacyO2PApi();
        robertO2P.setPlainUMLContextFromFile(
                contextModel.getAbsolutePath());
        robertO2P.setDescriptionMode(true);
        robertO2P.setContextualType("Car");

        O2P simpleO2P = new SimpleO2PApi();
        simpleO2P.setPlainUMLContextFromFile(
                contextModel.getAbsolutePath());
        simpleO2P.setDescriptionMode(true);
        simpleO2P.setContextualType("self", "Car");
        simpleO2P.setContextualType("kself", "Employee");
        simpleO2P.setContextualType("kcaller", "Employee");
       
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
                resWr.append("\n\n================ ~ BEGIN SIMPLE ~ ================\n\n");
                resWr.append(simpleCases[i]);
                resWr.append("\n\n===========================================");

                resWr.append("\n\n================ ~ Simple ~ ================\n\n");
                String simple = test(simpleO2P, simpleCases[i]);
                resWr.append(simple);

                resWr.append("\n================ ~ //END SIMPLE ~ ================\n\n");
            }
            
            resWr.flush();
        }
    }

    private static void test(OCL2PSQL ocl2psql, String oclExp)
            throws OclParseException, ParseException, IOException {
        String finalStatementWithDescription = ocl2psql.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
    }

    private static String test(O2P o2p, String oclExp) {
        String finalStatementWithDescription = o2p.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        return finalStatementWithDescription;
    }
    
    private static String[] legacyCases = { 
                "'Blue'",
                "1",
                "true",
                "self.Car:color",
                "self.Car:owners",
                "Car::allInstances()",
                "Car::allInstances()->collect(c| c.Car:color)",
                "Car::allInstances()->collect(c| c.Car:color)->exists(c| c.Car:color='yellow')",
                "Car::allInstances()->collect(c| c.Car:color.oclIsUndefined())",
                "Car::allInstances()->collect(c| c.Car:owners)->flatten()->collect(p| p.Person:ownedCars)->size()=5",
                "Car::allInstances()->collect(c| c.Car:owners)->flatten()->forAll(p| p.Person:name='Hoang')",
                "Car::allInstances()->collect(c| c.Car:owners.oclIsUndefined())",
                "Car::allInstances()->collect(c| false)->size() > 0",
                "Car::allInstances()->exists(c| c.Car:color = 'blue' )",
                "Car::allInstances()->exists(c| c.Car:owners->collect(p| p.Person:ownedCars)->size() < 2)",
                "Car::allInstances()->exists(c| c.Car:owners->exists(p| p.Person:name='Chau'))",
                "Car::allInstances()->exists(c| c.Car:owners->forAll(p| p.Person:name='Hoang'))",
                "Car::allInstances()->exists(c| true)",
                "Car::allInstances()->forAll(c| c.Car:color <> 'blue' )",
                "Car::allInstances()->forAll(c| c.Car:owners->collect(p| p.Person:name='Chau')->size() > 1)",
                "Car::allInstances()->forAll(c| c.Car:owners->exists(p| p.Person:name='Hoang'))",
                "Car::allInstances()->forAll(c| c.Car:owners->forAll(p| p.Person:name='Chau'))",
                "Car::allInstances()->forAll(c| false)",
                "Car::allInstances()->select(c| c.Car:color = 'blue')",
                "Car::allInstances()->select(c| true)",
                "Car::allInstances()->size() = 5",
                "Car::allInstances()->size()",

//                "Automobile::allInstances()",
//                "Automobile::allInstances()->collect(au|au)",
//                "Automobile::allInstances()->collect(au|au.Car:color)",
//                "Automobile::allInstances()->collect(au|au.Vehicle:category)",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Automobile))",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Bus))",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Car))",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Car).Car:color)",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Person))",
//                "Automobile::allInstances()->collect(au|au.oclAsType(Vehicle))",
//                "Automobile::allInstances()->collect(au|au.oclIsKindOf(Automobile))",
//                "Automobile::allInstances()->collect(au|au.oclIsKindOf(Car))",
//                "Automobile::allInstances()->collect(au|au.oclIsKindOf(Person))",
//                "Automobile::allInstances()->collect(au|au.oclIsKindOf(Vehicle))",
//                "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Automobile))",
//                "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Car))",
//                "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Person))",
//                "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Vehicle))",
//                "Car::allInstances()",
//                "Car::allInstances()->asSet()",
//                "Car::allInstances()->collect(c|c)",
//                "Car::allInstances()->collect(c|c.Car:color)",
//                "Car::allInstances()->collect(c|c.Car:color)->asSet()",
//                "Car::allInstances()->collect(c|c.Car:color)->isEmpty()",
//                "Car::allInstances()->collect(c|c.Car:color)->notEmpty()",
//                "Car::allInstances()->collect(c|c.Car:color)->size()",
//                "Car::allInstances()->collect(c|c.Car:owners)",
//                "Car::allInstances()->collect(c|c.Car:owners)->flatten()",
//                "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()",
//                "Car::allInstances()->collect(c|c.Car:owners)->size()",
//                "Car::allInstances()->collect(c|c.Car:owners->asSet())->flatten()",
//                "Car::allInstances()->collect(c|c.Car:owners->isEmpty())",
//                "Car::allInstances()->collect(c|c.Car:owners->notEmpty())",
//                "Car::allInstances()->exists(c|c.Car:color <> 'no-color')",
//                "Car::allInstances()->exists(c|c.Car:owners->exists(p|p = self))",
//                "Car::allInstances()->exists(c|c.Car:owners->exists(p|p.Person:ownedCars->size() < self.Person:ownedCars->size()))",
//                "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))",
//                "Car::allInstances()->exists(c|c.Car:owners->isEmpty())",
//                "Car::allInstances()->exists(c|c.Car:owners->notEmpty())",
//                "Car::allInstances()->forAll(c|'blue'=c.Car:color)",
//                "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p)->size() = 1)",
//                "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p.Person:ownedCars)->flatten()->size()=1)",
//                "Car::allInstances()->forAll(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))",
//                "Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1.Car:color=c.Car:color)))",
//                "Car::allInstances()->forAll(c|c.Car:owners->isEmpty())",
//                "Car::allInstances()->forAll(c|c.Car:owners->notEmpty())",
//                "Car::allInstances()->forAll(c|c.Car:owners->select(p|p.Person:name.oclIsUndefined())->size()=0)",
//                "Car::allInstances()->isEmpty()",
//                "Car::allInstances()->notEmpty()",
//                "Car::allInstances()->select(c|c.Car:color = 'no-color')",
//                "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()",
//                "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())",
//                "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()",
//                "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()",
//                "Car::allInstances()->size()",
//                "Person::allInstances()->collect(p|p.Person:ownedCars->size())",
//                "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))",
//                "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))",
//                "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)",
//                "Person::allInstances()->exists(p|p.Person:ownedCars->size() = 10)",
//                "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))",
//                "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))",
//                "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)",
//                "Person::allInstances()->forAll(p|true)",
//                "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))",
//                "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))",
//                "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))",
//                "Vehicle::allInstances()",
//                "self = caller",
//                "self",
//                "self.Person:name = 'Hoang'",
//                "self.Person:name",
//                "self.Person:ownedCars",
//                "self.Person:ownedCars->exists(c|c.Car:color = 'black')",
//                "self.Person:ownedCars->size()",
//                "true",
    };

    private static String[] simpleCases = { 
                "'Blue'",
                "1",
                "true",
                "self.color",
                "self.owners",
                "Car.allInstances()",
                "Car.allInstances()->collect(c| c.color)",
                "Car.allInstances()->collect(c| c.color)->exists(c| c ='yellow')",
                "Car.allInstances()->collect(c| c.color.oclIsUndefined())",
                "Car.allInstances()->collect(c| c.owners)->flatten()->collect(p| p.ownedCars)->size()=5",
                "Car.allInstances()->collect(c| c.owners)->flatten()->forAll(p| p.name='Hoang')",
                "Car.allInstances()->collect(c| c.owners.oclIsUndefined())",
                "Car.allInstances()->collect(c| false)->size() > 0",
                "Car.allInstances()->exists(c| c.color = 'blue' )",
                "Car.allInstances()->exists(c| c.owners->collect(p| p.ownedCars)->size() < 2)",
                "Car.allInstances()->exists(c| c.owners->exists(p| p.name='Chau'))",
                "Car.allInstances()->exists(c| c.owners->forAll(p| p.name='Hoang'))",
                "Car.allInstances()->exists(c| true)",
                "Car.allInstances()->forAll(c| c.color <> 'blue' )",
                "Car.allInstances()->forAll(c| c.owners->collect(p| p.name='Chau')->size() > 1)",
                "Car.allInstances()->forAll(c| c.owners->exists(p| p.name='Hoang'))",
                "Car.allInstances()->forAll(c| c.owners->forAll(p| p.name='Chau'))",
                "Car.allInstances()->forAll(c| false)",
                "Car.allInstances()->select(c| c.color = 'blue')",
                "Car.allInstances()->select(c| true)",
                "Car.allInstances()->size() = 5",
                "Car.allInstances()->size()",
//                "kself = kcaller or kself.salary < 60000 or kself.age < 30",
//                "kself = kcaller or kself.salary < 60000",
//                "kself = kcaller or kself.age < 30",


//                "Automobile.allInstances()",
//                "Automobile.allInstances()->collect(au|au)",
//                "Automobile.allInstances()->collect(au|au.color)",
//                "Automobile.allInstances()->collect(au|au.category)",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Automobile))",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Bus))",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Car))",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Car).Car:color)",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Person))",
//                "Automobile.allInstances()->collect(au|au.oclAsType(Vehicle))",
//                "Automobile.allInstances()->collect(au|au.oclIsKindOf(Automobile))",
//                "Automobile.allInstances()->collect(au|au.oclIsKindOf(Car))",
//                "Automobile.allInstances()->collect(au|au.oclIsKindOf(Person))",
//                "Automobile.allInstances()->collect(au|au.oclIsKindOf(Vehicle))",
//                "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Automobile))",
//                "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Car))",
//                "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Person))",
//                "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Vehicle))",
//                "Car.allInstances()",
//                "Car.allInstances()->asSet()",
//                "Car.allInstances()->collect(c|c)",
//                "Car.allInstances()->collect(c|c.color)",
//                "Car.allInstances()->collect(c|c.color)->asSet()",
//                "Car.allInstances()->collect(c|c.color)->isEmpty()",
//                "Car.allInstances()->collect(c|c.color)->notEmpty()",
//                "Car.allInstances()->collect(c|c.color)->size()",
//                "Car.allInstances()->collect(c|c.owners)",
//                "Car.allInstances()->collect(c|c.owners)->flatten()",
//                "Car.allInstances()->collect(c|c.owners)->flatten()->size()",
//                "Car.allInstances()->collect(c|c.owners)->size()",
//                "Car.allInstances()->collect(c|c.owners->asSet())->flatten()",
//                "Car.allInstances()->collect(c|c.owners->isEmpty())",
//                "Car.allInstances()->collect(c|c.owners->notEmpty())",
//                "Car.allInstances()->exists(c|c.color <> 'no-color')",
//                "Car.allInstances()->exists(c|c.owners->exists(p|p = self))",
//                "Car.allInstances()->exists(c|c.owners->exists(p|p.ownedCars->size() < self.ownedCars->size()))",
//                "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'no-name'))",
//                "Car.allInstances()->exists(c|c.owners->isEmpty())",
//                "Car.allInstances()->exists(c|c.owners->notEmpty())",
//                "Car.allInstances()->forAll(c|'blue'=c.color)",
//                "Car.allInstances()->forAll(c|c.owners->collect(p|p)->size() = 1)",
//                "Car.allInstances()->forAll(c|c.owners->collect(p|p.ownedCars)->flatten()->size()=1)",
//                "Car.allInstances()->forAll(c|c.owners->exists(p|p.name = 'no-name'))",
//                "Car.allInstances()->forAll(c|c.owners->forAll(p|p.ownedCars->forAll(c1|c1.color=c.color)))",
//                "Car.allInstances()->forAll(c|c.owners->isEmpty())",
//                "Car.allInstances()->forAll(c|c.owners->notEmpty())",
//                "Car.allInstances()->forAll(c|c.owners->select(p|p.name.oclIsUndefined())->size()=0)",
//                "Car.allInstances()->isEmpty()",
//                "Car.allInstances()->notEmpty()",
//                "Car.allInstances()->select(c|c.color = 'no-color')",
//                "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//                "Car.allInstances()->select(c|c.color.oclIsUndefined())",
//                "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//                "Car.allInstances()->select(c|c.owners->exists(p|p.name.oclIsUndefined()))->size()",
//                "Car.allInstances()->size()",
//                "Person.allInstances()->collect(p|p.ownedCars->size())",
//                "Person.allInstances()->exists(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//                "Person.allInstances()->exists(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//                "Person.allInstances()->exists(p|p.ownedCars->size() < 10)",
//                "Person.allInstances()->exists(p|p.ownedCars->size() = 10)",
//                "Person.allInstances()->forAll(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//                "Person.allInstances()->forAll(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//                "Person.allInstances()->forAll(p|p.ownedCars->size() > 0)",
//                "Person.allInstances()->forAll(p|true)",
//                "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color <> 'no-color'))",
//                "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color.oclIsUndefined()))",
//                "Person.allInstances()->select(p|p.ownedCars->forAll(c|c.color <> 'no-color'))",
//                "Vehicle.allInstances()",
//                "self = caller",
//                "self",
//                "self.name = 'Hoang'",
//                "self.name",
//                "self.ownedCars",
//                "self.ownedCars->exists(c|c.color = 'black')",
//                "self.ownedCars->size()",
//                "true",
    };

}
