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

@author: ngpbh
***************************************************************************/


package com.vgu.se.jocl.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

public class UMLContextUtilsTest {
    
    JSONArray sourceArray = null;

    @Before
    public void setUp() throws Exception {
        sourceArray = (JSONArray) new JSONParser().parse("[\r\n" + 
                "  {\r\n" + 
                "    \"class\":\"Car\",\r\n" + 
                "    \"attributes\":[{\"name\":\"color\", \"type\":\"String\"}]\r\n" + 
                "  },\r\n" + 
                "  {\r\n" + 
                "    \"class\":\"Person\",\r\n" + 
                "    \"attributes\":[{\"name\":\"name\", \"type\":\"String\"}]\r\n" + 
                "  },\r\n" + 
                "  {\r\n" + 
                "    \"association\":\"Ownership\",\r\n" + 
                "    \"ends\":[\"owners\", \"ownedCars\"],\r\n" + 
                "    \"classes\":[\"Car\", \"Person\"]\r\n" + 
                "  }\r\n" + 
                "]");
    }

    @Test
    public void isClassTest() {
        assertTrue(UMLContextUtils.isClass(sourceArray, "Car"));
        assertTrue(UMLContextUtils.isClass(sourceArray, "Person"));
        assertFalse(UMLContextUtils.isClass(sourceArray, "Ownership"));
        assertFalse(UMLContextUtils.isClass(sourceArray, "color"));
        assertFalse(UMLContextUtils.isClass(sourceArray, "car"));
        assertFalse(UMLContextUtils.isClass(sourceArray, null));
    }
    
    @Test
    public void isAssociationClassTest() {
        assertFalse(UMLContextUtils.isAssociationClass(sourceArray, "Car"));
        assertFalse(UMLContextUtils.isAssociationClass(sourceArray, "owners"));
        assertTrue(UMLContextUtils.isAssociationClass(sourceArray, "Ownership"));
        assertFalse(UMLContextUtils.isAssociationClass(sourceArray, "color"));
        assertFalse(UMLContextUtils.isAssociationClass(sourceArray, "car"));
        assertFalse(UMLContextUtils.isAssociationClass(sourceArray, null));
    }

    @Test
    public void isPropertyOfClassTest() {
        assertTrue(UMLContextUtils.isPropertyOfClass(sourceArray, "Car", "color"));
        assertFalse(UMLContextUtils.isPropertyOfClass(sourceArray, "Car", "owners"));
        assertFalse(UMLContextUtils.isPropertyOfClass(sourceArray, "Car", "name"));
        assertTrue(UMLContextUtils.isPropertyOfClass(sourceArray, "Person", "name"));
        assertFalse(UMLContextUtils.isPropertyOfClass(sourceArray, "Ownership", "owners"));
    }
    
    @Test
    public void isAssociationOfClassTest() {
        assertFalse(UMLContextUtils.isAssociationOfClass(sourceArray, "Car", "color"));
        assertFalse(UMLContextUtils.isAssociationOfClass(sourceArray, "Ownership", "Car"));
        assertFalse(UMLContextUtils.isAssociationOfClass(sourceArray, "Ownership", "owners"));
        assertFalse(UMLContextUtils.isAssociationOfClass(sourceArray, "Ownership", "ownedCars"));
        assertTrue(UMLContextUtils.isAssociationOfClass(sourceArray, "Car", "ownedCars"));
        assertFalse(UMLContextUtils.isAssociationOfClass(sourceArray, "Car", "owners"));
        assertTrue(UMLContextUtils.isAssociationOfClass(sourceArray, "Person", "owners"));
    }
}
