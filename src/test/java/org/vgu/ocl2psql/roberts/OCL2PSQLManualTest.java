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
//            "src/main/resources/context-model/CarPerson_context-new-model.json");
        // This is for the new model
                "src/main/resources/genSQL/uni_pof_dm.json");
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
        simpleO2P.setDescriptionMode(false);
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
        String finalStatementWithDescription = o2p.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
        return finalStatementWithDescription;
    }

    private static String[] simpleCases = {
//        "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//        "Car.allInstances()->exists(c|c.color <> 'no-color')",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//        
//        "Car.allInstances()->collect(c|c)",
//        "Car.allInstances()->forAll(c|c.owners->collect(p|p.ownedCars)->flatten()->size()=1)",
//        "Car.allInstances()->forAll(c|c.owners->select(p|p.name.oclIsUndefined())->size()=0)",
//        "Car.allInstances()->collect(c|c.color)",
//        "Car.allInstances()->collect(c|c.owners)",
//        "Car.allInstances()->collect(c|c.owners)->flatten()",
//        "Car.allInstances()->forAll(c|c.owners->collect(p|p)->size() = 1)",
//        "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//        "Car.allInstances()->exists(c|c.color <> 'no-color')",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//        "Car.allInstances()->forAll(c|c.owners->exists(p|p.name = 'no-name'))",
//        "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'no-name'))",
//
//        "Car.allInstances()",
//        "true",
//        "Car.allInstances()->collect(c|c.color)",
//        "Car.allInstances()->collect(c|c.owners)",
//        "Car.allInstances()->collect(c|c.owners)->flatten()",
//
//        "Car.allInstances()->size()",
//        "Car.allInstances()->collect(c|c.color)->size()",
//        "Person.allInstances()->collect(p|p.ownedCars->size())",
//        "Car.allInstances()->collect(c|c.owners)->flatten()",
//        "Car.allInstances()->collect(c|c.owners)->flatten()->size()",
//        "Car.allInstances()->collect(c|c.owners)->size()",
//
//        "Person.allInstances()->forAll(p|p.ownedCars->size() > 0)",
//        "Person.allInstances()->forAll(p|true)",
//        "Person.allInstances()->exists(p|p.ownedCars->size() < 10)",
//        "Person.allInstances()->forAll(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//        "Person.allInstances()->forAll(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//        "Person.allInstances()->exists(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//        "Person.allInstances()->exists(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//
//        "Car.allInstances()->select(c|c.color = 'no-color')",
//        "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color <> 'no-color'))",
//        "Person.allInstances()->select(p|p.ownedCars->forAll(c|c.color <> 'no-color'))",
//
//        "Car.allInstances()->select(c|c.color.oclIsUndefined())",
//        "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color.oclIsUndefined()))",
//
//        "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//        "Car.allInstances()->exists(c|c.color <> 'no-color')",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//        "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'no-name'))",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name.oclIsUndefined()))->size()",
//        "Car.allInstances()",
//        "true",
//        "Car.allInstances()->collect(c|c.color)",
//        "Car.allInstances()->collect(c|c.owners)",
//        "Car.allInstances()->collect(c|c.owners)->flatten()",
//
//        "Car.allInstances()->size()",
//        "Car.allInstances()->collect(c|c.color)->size()",
//        "Person.allInstances()->collect(p|p.ownedCars->size())",
//        "Car.allInstances()->collect(c|c.owners)->flatten()",
//        "Car.allInstances()->collect(c|c.owners)->flatten()->size()",
//        "Car.allInstances()->collect(c|c.owners)->size()",
//
//        "Person.allInstances()->forAll(p|p.ownedCars->size() > 0)",
//        "Person.allInstances()->exists(p|p.ownedCars->size() = 10)",
//        "Person.allInstances()->exists(p|p.ownedCars->size() < 10)",
//        "Person.allInstances()->forAll(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//        "Person.allInstances()->forAll(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//        "Person.allInstances()->exists(p|p.ownedCars->forAll(c|c.color = 'no-color'))",
//        "Person.allInstances()->exists(p|p.ownedCars->exists(c|c.color = 'no-color'))",
//
//        "Car.allInstances()->select(c|c.color = 'no-color')",
//        "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color <> 'no-color'))",
//        "Person.allInstances()->select(p|p.ownedCars->forAll(c|c.color <> 'no-color'))",
//
//        "Car.allInstances()->select(c|c.color.oclIsUndefined())",
//        "Person.allInstances()->select(p|p.ownedCars->exists(c|c.color.oclIsUndefined()))",
//
//        "Car.allInstances()->select(c|c.color = 'no-color')->size()",
//        "Car.allInstances()->exists(c|c.color <> 'no-color')",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name = 'no-name'))->size()",
//        "Car.allInstances()->exists(c|c.owners->forAll(p|p.name = 'no-name'))",
//        "Car.allInstances()->select(c|c.owners->exists(p|p.name.oclIsUndefined()))->size()",
//
//        "Car.allInstances()->isEmpty()",
//        "Car.allInstances()->collect(c|c.color)->isEmpty()",
//        "Car.allInstances()->collect(c|c.owners->isEmpty())",
//        "Car.allInstances()->forAll(c|c.owners->isEmpty())",
//        "Car.allInstances()->exists(c|c.owners->isEmpty())",
//
//        "Car.allInstances()->notEmpty()",
//        "Car.allInstances()->collect(c|c.color)->notEmpty()",
//        "Car.allInstances()->collect(c|c.owners->notEmpty())",
//        "Car.allInstances()->forAll(c|c.owners->notEmpty())",
//        "Car.allInstances()->exists(c|c.owners->notEmpty())",

//        "Car.allInstances()->forAll(c|c.owners->forAll(p|p.ownedCars->forAll(c1|c1.color=c.color)))",
//        "Car.allInstances()->forAll(c|'blue'=c.color)",
        
        //context
//        "self",
//        "self = caller",
//        "self.name",
//        "self.name = 'Hoang'",
//        "self.ownedCars",
//        "self.ownedCars->size()",
//        "self.ownedCars->exists(c|c.color = 'black')",
//        "Car.allInstances()->exists(c|c.owners->exists(p|p = self))",
//        "Car.allInstances()->exists(c|c.owners->exists(p|p.ownedCars->size() < self.ownedCars->size()))",
        
        //assocs
//        "Exam.allInstances()->collect(e|e.rooms)"
//        "Room.allInstances()->collect(r|r.exams)"
//        "Program.allInstances()->collect(p|p.university)"
//        "Module_Period.allInstances()->collect(mp|mp.exam)"

        //asSet
//      "Car.allInstances()->asSet()",
//      "Car.allInstances()->collect(c|c.color)->asSet()",
//      "Car.allInstances()->collect(c|c.owners->asSet())->flatten()",

        //Generalization
//        "Car.allInstances()",
//        "Automobile.allInstances()",
//        "Vehicle.allInstances()",
//        "Automobile.allInstances()->collect(au|au)",
//        "Automobile.allInstances()->collect(au|au.category)",
//        "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Automobile))",
//        "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Car))",
//        "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Person))",
//        "Automobile.allInstances()->collect(au|au.oclIsTypeOf(Vehicle))",
//        "Automobile.allInstances()->collect(au|au.oclIsKindOf(Automobile))",
//        "Automobile.allInstances()->collect(au|au.oclIsKindOf(Car))",
//        "Automobile.allInstances()->collect(au|au.oclIsKindOf(Person))",
//        "Automobile.allInstances()->collect(au|au.oclIsKindOf(Vehicle))",
//        "Automobile.allInstances()->collect(au|au.oclAsType(Automobile))",
//        "Automobile.allInstances()->collect(au|au.oclAsType(Car))",
//        "Automobile.allInstances()->collect(au|au.oclAsType(Bus))",
//        "Automobile.allInstances()->collect(au|au.oclAsType(Car).color)"   
        //University test
//        "Program.allInstances()->forAll(p|p.doe < @SQL(CURDATE()))",
//        "Enrollment.allInstances()->forAll(e|e.ends.oclIsUndefined() or e.ends > e.starts)",
//        "Enrollment.allInstances()->forAll(e|e.starts > e.program.doe)"
//        "Program.allInstances()->forAll(p|p.modules->collect(m|m.name)->isUnique())"
            
//            "Student.allInstances()->collect(s|@SQL(CURDATE()))",
//            "@SQL(TimeStampDiff(year, @SQL(curdate()) , @SQL(curdate()) ))",
            "Student.allInstances()->exists(s| @SQL(TIMESTAMPDIFF(year, s.dob, @SQL(CURDATE()))) > 18)",
    };

}
