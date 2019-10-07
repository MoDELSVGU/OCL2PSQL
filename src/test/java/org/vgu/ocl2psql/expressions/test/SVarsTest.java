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


package org.vgu.ocl2psql.expressions.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.vgu.ocl2psql.main.OCL2PSQL;
import org.vgu.ocl2psql.ocl.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.exception.OclParseException;
import org.vgu.ocl2psql.ocl.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.expressions.OperationCallExp;
import org.vgu.ocl2psql.ocl.expressions.VariableUtils;
import org.vgu.ocl2psql.ocl.visitor.OCL2SQLParser;

public class SVarsTest {

    private static OCL2PSQL ocl2psql = new OCL2PSQL();
    
    static {
        File contextModel = new File("./src/main/resources/context-model/old-CarPerson_context.json");
        try {
            ocl2psql.setPlainUMLContextFromFile(contextModel.getAbsolutePath());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_Car_allInstances() throws OclParseException {
        String testCase ="Car::allInstances()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((OperationCallExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_isUnique() throws OclParseException {
        String testCase ="Car::allInstances()->isUnique()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    

    @Test
    public void test_Car_allInstances_size() throws OclParseException {
        String testCase ="Car::allInstances()->size()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_isEmpty() throws OclParseException {
        String testCase ="Car::allInstances()->isEmpty()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_notEmpty() throws OclParseException {
        String testCase ="Car::allInstances()->notEmpty()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect_body() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:color)";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect_source() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:color)";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_select_body() throws OclParseException {
        String testCase ="Car::allInstances()->select(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_select_source() throws OclParseException {
        String testCase ="Car::allInstances()->select(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_forAll_body() throws OclParseException {
        String testCase ="Car::allInstances()->forAll(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_forAll_source() throws OclParseException {
        String testCase ="Car::allInstances()->forAll(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_reject_body() throws OclParseException {
        String testCase ="Car::allInstances()->reject(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_reject_source() throws OclParseException {
        String testCase ="Car::allInstances()->reject(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_body() throws OclParseException {
        String testCase ="Car::allInstances()->exists(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_source() throws OclParseException {
        String testCase ="Car::allInstances()->exists(c|c.Car:color='blue')";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>();
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_forAll_body() throws OclParseException {
        String testCase ="Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name='Hoang'))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect_forAll_source() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:owners->forAll(p|p.Person:name='Hoang'))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c","p"));
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }

    @Test
    public void test_Car_allInstances_collect_forAll_body() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:owners->forAll(p|p.Person:name='Hoang'))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_collect_oclIsUndefined() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:color.oclIsUndefined())";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) exp).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }

    @Test
    public void test_Car_allInstances_collect_isEmpty() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:color->isEmpty())";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c") );
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_forAll_source() throws OclParseException {
        String testCase ="Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name='Hoang'))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c","p"));
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_forAll_2_level() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:owners->forAll(p|p.Person:name=c.Car:color))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getSource();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c"));
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    @Test
    public void test_Car_allInstances_exists_forAll_2_level_2() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:owners->forAll(p|p.Person:name=c.Car:color))";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) exp).getBody()).getBody();
        List<String> actualSVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedSVars = new ArrayList<String>(Arrays.asList("c", "p"));
        boolean isValid = actualSVars.size() == expectedSVars.size();
        for(int i = 0; i < actualSVars.size() && isValid; i++) {
            isValid = expectedSVars.contains(actualSVars.get(i));
        }
        assertTrue(isValid);
    }
    
    

    @Test
    public void test_Car_allInstances_collect_asSet_flatten() throws OclParseException {
        String testCase ="Car::allInstances()->collect(c|c.Car:owners->asSet())->flatten()";
        OclExpression exp = OclExpression.parse( testCase, new DefaultOclContext() );
        OCL2SQLParser ocl2sqlParser = ocl2psql.getOcl2sqlParser();
        ocl2sqlParser.visit( exp );

        OclExpression testExp = ((IteratorExp) ((IteratorExp) ((IteratorExp) exp).getSource()).getBody()).getSource();
        List<String> actualFVars = VariableUtils.SVars( testExp, ocl2sqlParser );
        List<String> expectedFVars = new ArrayList<String>(Arrays.asList( "c" ));
        boolean isValid = actualFVars.size() == expectedFVars.size();
        for(int i = 0; i < actualFVars.size() && isValid; i++) {
            isValid = expectedFVars.contains(actualFVars.get(i));
        }
        assertTrue(isValid);
    }
    

}
