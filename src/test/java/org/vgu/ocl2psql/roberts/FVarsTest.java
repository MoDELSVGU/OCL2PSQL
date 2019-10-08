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


package org.vgu.ocl2psql.roberts;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.vgu.ocl2psql.ocl.roberts.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.roberts.parse.SimpleParser;
import org.vgu.ocl2psql.ocl.roberts.utils.VariableUtils;

public class FVarsTest {
    
    @Test
    public void test_Car_allInstances() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_isUnique() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->isUnique()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_size() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->size()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_isEmpty() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->isEmpty()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_notEmpty() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->notEmpty()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:color)", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_select() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->select(c|c.Car:color = 'blue')", new DefaultOclContext());
        boolean isValid = true;
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        for(String v : actualFVars) {
            expectedFVars.contains(v);
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_forAll() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:color = 'blue')", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->exists(c|c.Car:color = 'blue')", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_reject() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->reject(c|c.Car:color = 'blue')", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect_flatten() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners)->flatten()", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_att() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:color)", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners)", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c)", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_Car_allInstances() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|Car::allInstances())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_att_binary_comparison() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:color = 'blue')", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_att_unary_comparison() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:color.oclIsUndefined())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_isEmpty() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->isEmpty())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_notEmpty() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->notEmpty())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_isUnique() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->isUnique())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_size() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->size())", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }

    @Test
    public void test_body_var_assoc_select() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->select(p|p=p))->flatten()", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) ((IteratorExp) exp).getSource()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_forAll() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->forAll(p|p = p))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_reject() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->reject(p|p = p))->flatten()", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) ((IteratorExp) exp).getSource()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_var_assoc_exists() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->exists(p|p = p))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_second_level_source() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->forAll(p|p = p))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp) exp).getBody()).getSource();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_second_level_body() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->forAll(p|p=p))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp) exp).getBody()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("p");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_second_level_body_2() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->collect(c|c.Car:owners->forAll(p|p.Person:name=c.Car:color))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp) exp).getBody()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("p","c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        List<String> actualFVars = VariableUtils.FVars(exp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_1() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getSource();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = new ArrayList<String>();
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_2() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp) exp).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_3() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp) exp).getBody()).getSource();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_4() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp) exp).getBody()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("p");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_5() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp)((IteratorExp) exp).getBody()).getBody()).getSource();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("p");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_body_three_levels_6() throws OclParseException {
        OclExpression exp = SimpleParser.parse("Car::allInstances()->forAll(c|c.Car:owners->forAll(p|p.Person:ownedCars->forAll(c1|c1=c)))", new DefaultOclContext());
        OclExpression subExp = ((IteratorExp)((IteratorExp)((IteratorExp) exp).getBody()).getBody()).getBody();
        List<String> actualFVars = VariableUtils.FVars(subExp);
        List<String> expectedFVars = Arrays.asList("c1", "c");
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
}
