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

import net.sf.jsqlparser.statement.select.Select;

public class TesterOCL2SQLParser {

    public static void main(String[] args)
            throws Exception {
        String resourceName = "config.properties"; // could also be a constant
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(resourceName)){
            props.load(resourceStream);

            OCL2PSQL ocl2psql = new OCL2PSQL();
            ocl2psql.setPlainUMLContextFromFile(props.getProperty("cardb.filePath"));
            
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p.Person:ownedCars)->flatten()->size()=1)");
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->select(p|p.Person:name.oclIsUndefined())->size()=0)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p)->size() = 1)");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
            
            test(ocl2psql, "Car::allInstances()");
            test(ocl2psql, "true");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
    
            test(ocl2psql, "Car::allInstances()->size()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->size()");
            test(ocl2psql, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->size()");
    
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
            test(ocl2psql, "Person::allInstances()->forAll(p|true)");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
            test(ocl2psql, "Car::allInstances()");
            test(ocl2psql, "true");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
    
            test(ocl2psql, "Car::allInstances()->size()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->size()");
            test(ocl2psql, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners)->size()");
    
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() = 10)");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
            test(ocl2psql, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
            test(ocl2psql, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");
    
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
            test(ocl2psql, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
        
            test(ocl2psql, "Car::allInstances()->isEmpty()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->isEmpty()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->isEmpty())");
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->isEmpty())");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->isEmpty())");
            
            test(ocl2psql, "Car::allInstances()->notEmpty()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->notEmpty()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->notEmpty())");
            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->notEmpty())");
            test(ocl2psql, "Car::allInstances()->exists(c|c.Car:owners->notEmpty())");
            
            test(ocl2psql, "Car::allInstances()->asSet()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:color)->asSet()");
            test(ocl2psql, "Car::allInstances()->collect(c|c.Car:owners->asSet())->flatten()");

            test(ocl2psql, "Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1.Car:color=c.Car:color)))");
            test(ocl2psql, "Car::allInstances()->forAll(c|'blue'=c.Car:color)");
        }
    }

    private static void test(OCL2PSQL ocl2psql, String oclExp) throws OclParseException {
        System.out.println(oclExp);
        Select finalStatement = ocl2psql.mapToSQL(oclExp);
        System.out.println(finalStatement);
        System.out.println();
    }
}
