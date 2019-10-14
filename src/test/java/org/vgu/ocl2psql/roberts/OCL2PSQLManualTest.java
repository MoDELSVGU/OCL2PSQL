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
import java.io.IOException;
import java.io.InputStream;
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

        File contextModel = new File("src/main/resources/context-model/CarPerson_context.json");

        OCL2PSQL ocl2psql = new OCL2PSQL();
        ocl2psql.setPlainUMLContextFromFile(contextModel.getAbsolutePath());
        ocl2psql.setDescriptionMode(true);
        
        O2P robertO2P = new LegacyO2PApi();
        robertO2P.setPlainUMLContextFromFile(
                contextModel.getAbsolutePath());
        robertO2P.setDescriptionMode(true);

        O2P simpleO2P = new SimpleO2PApi();
        simpleO2P.setPlainUMLContextFromFile(
                contextModel.getAbsolutePath());
        simpleO2P.setDescriptionMode(true);
        
//        test(ocl2psql, "Car::allInstances()->collect(c|c)");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p.Person:ownedCars)->flatten()->size()=1)");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->select(p|p.Person:name.oclIsUndefined())->size()=0)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p)->size() = 1)");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
//        
//        test(ocl2psql, "Car::allInstances()");
//        test(ocl2psql, "true");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
//
//        test(ocl2psql, "Car::allInstances()->size()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->size()");
//        test(ocl2psql, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->size()");
//
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
//        test(ocl2psql, "Person::allInstances()->forAll(p|true)");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
//        test(ocl2psql, "Car::allInstances()");
//        test(ocl2psql, "true");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
//
//        test(ocl2psql, "Car::allInstances()->size()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->size()");
//        test(ocl2psql, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->size()");
//
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() = 10)");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
//        test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");
//
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
//        test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
//    
//        test(ocl2psql, "Car::allInstances()->isEmpty()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->isEmpty()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->isEmpty())");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->isEmpty())");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->isEmpty())");
//        
//        test(ocl2psql, "Car::allInstances()->notEmpty()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->notEmpty()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->notEmpty())");
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->notEmpty())");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->notEmpty())");
//        
//        test(ocl2psql, "Car::allInstances()->asSet()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->asSet()");
//        test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->asSet())->flatten()");
//
//        test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1.Car:color=c.Car:color)))");
//        test(ocl2psql, "Car::allInstances()->forAll(c|'blue'=c.Car:color)");
//        
//        test(ocl2psql, "self");
//        test(ocl2psql, "self = caller");
//        test(ocl2psql, "self.Person:name");
//        test(ocl2psql, "self.Person:name = 'Hoang'");
//        test(ocl2psql, "self.Person:ownedCars");
//        test(ocl2psql, "self.Person:ownedCars->size()");
//        test(ocl2psql, "self.Person:ownedCars->exists(c|c.Car:color = 'black')");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->exists(p|p = self))");
//        test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->exists(p|p.Person:ownedCars->size() < self.Person:ownedCars->size()))");
//    
//        test(ocl2psql, "Car::allInstances()");
//        test(ocl2psql, "Automobile::allInstances()");
//        test(ocl2psql, "Vehicle::allInstances()");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au)");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.Vehicle:category)");
////            test(ocl2psql, "Automobile::allInstances()->collect(au|au.Car:color)");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Automobile))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Car))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Person))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsTypeOf(Vehicle))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsKindOf(Automobile))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsKindOf(Car))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsKindOf(Person))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclIsKindOf(Vehicle))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Automobile))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Car))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Bus))");
////            test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Person))");
////            test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Vehicle))");
//        test(ocl2psql, "Automobile::allInstances()->collect(au|au.oclAsType(Car).Car:color)");
        
//        ocl2psql.setNewParser(true);
//        test(ocl2psql, "Car.allInstances()");
        
        test(robertO2P, "Car::allInstances()->size()");
        test(simpleO2P, "Car.allInstances()");
    }

    private static void test(OCL2PSQL ocl2psql, String oclExp) throws OclParseException, ParseException, IOException {
        String finalStatementWithDescription = ocl2psql.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
    }

    private static void test(O2P o2p, String oclExp) {
        String finalStatementWithDescription = o2p.mapToString(oclExp);
        System.out.println(finalStatementWithDescription);
    }
}
