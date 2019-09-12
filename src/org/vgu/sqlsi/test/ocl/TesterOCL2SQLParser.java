package org.vgu.sqlsi.test.ocl;

import java.util.ArrayList;
import java.util.List;

import org.vgu.sqlsi.ocl.context.DefaultOclContext;
import org.vgu.sqlsi.ocl.exception.OclParseException;
import org.vgu.sqlsi.ocl.exception.SetOfSetException;
import org.vgu.sqlsi.ocl.expressions.IteratorSource;
import org.vgu.sqlsi.ocl.expressions.OclExpression;
import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;
import org.vgu.sqlsi.sql.statement.select.ValSelectExpression;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class TesterOCL2SQLParser {

    public static void main(String[] args)
            throws Exception {

        /*
         * Note: the file programDB_context.json is needed for the examples below it can
         * be found in Documentation
         */
        String filePath = "/Users/thian/prjcts/eclipseWork/OCL2PSQL/Documentation/CarPerson_context.json";
//        String filePath = "C:\\Users\\ngpbh\\eclipse-workspace\\sqlsi\\Documentation\\CarPerson_context.json";
//        String filePath = "/Users/clavel/VGU/Repositories/SqlSI/Documentation/CarPerson_context.json";

        OCL2SQLParser ocl2psqlParser = new OCL2SQLParser();
        ocl2psqlParser.setPlainUMLContextFromFile(filePath);
        
        test(ocl2psqlParser, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p.Person:ownedCars)->flatten()->size()=1)");
        test(ocl2psqlParser, "Car::allInstances()->forAll(c|c.Car:owners->select(p|p.Person:name.oclIsUndefined())->size()=0)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:color)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
        test(ocl2psqlParser, "Car::allInstances()->forAll(c|c.Car:owners->collect(p|p)->size() = 1)");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
        test(ocl2psqlParser, "Car::allInstances()->forAll(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
        
        test(ocl2psqlParser, "Car::allInstances()");
        test(ocl2psqlParser, "true");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:color)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");

        test(ocl2psqlParser, "Car::allInstances()->size()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:color)->size()");
        test(ocl2psqlParser, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->size()");

        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
        test(ocl2psqlParser, "Person::allInstances()->forAll(p|true)");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
        test(ocl2psqlParser, "Car::allInstances()");
        test(ocl2psqlParser, "true");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:color)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");

        test(ocl2psqlParser, "Car::allInstances()->size()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:color)->size()");
        test(ocl2psqlParser, "Person::allInstances()->collect(p|p.Person:ownedCars->size())");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->flatten()->size()");
        test(ocl2psqlParser, "Car::allInstances()->collect(c|c.Car:owners)->size()");

        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->size() > 0)");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->size() = 10)");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->size() < 10)");
        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->forAll(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->forAll(c|c.Car:color = 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->exists(p|p.Person:ownedCars->exists(c|c.Car:color = 'no-color'))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color = 'no-color')");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color <> 'no-color'))");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->forAll(c|c.Car:color <> 'no-color'))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color.oclIsUndefined())");
        test(ocl2psqlParser, "Person::allInstances()->select(p|p.Person:ownedCars->exists(c|c.Car:color.oclIsUndefined()))");

        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:color = 'no-color')->size()");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:color <> 'no-color')");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name = 'no-name'))->size()");
        test(ocl2psqlParser, "Car::allInstances()->exists(c|c.Car:owners->forAll(p|p.Person:name = 'no-name'))");
        test(ocl2psqlParser, "Car::allInstances()->select(c|c.Car:owners->exists(p|p.Person:name.oclIsUndefined()))->size()");
    }

    private static void test(OCL2SQLParser ocl2psqlParser, String oclExp) throws OclParseException {
        OclExpression exp;
        ocl2psqlParser.resetLevelOfSet();
        ocl2psqlParser.setVisitorContext(new ArrayList<IteratorSource>());
        exp = OclExpression.parse(oclExp, new DefaultOclContext());
        System.out.println(oclExp);
        try {
            Select finalStatement = (Select) ocl2psqlParser.visit(exp);
//            if(ocl2psqlParser.getLevelOfSets() > 1) {
//                System.out.println("Invalid output sets of sets!!");
//                System.out.println("==============================================");
//                return;
//            }
            cookFinalStatement(finalStatement);
            System.out.println(finalStatement);
            System.out.println("==============================================");
        } catch (SetOfSetException e) {
            System.out.println("Invalid output sets of sets!!");
            System.out.println("==============================================");
            return;
        }
    }

    private static void cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement.getSelectBody();
        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();
        for(SelectItem item : finalPlainSelect.getSelectItems()) {
            if(item instanceof ResSelectExpression || item instanceof ValSelectExpression) {
                newSelectItems.add(item);
            } else {
//                RefSelectExpression refExpression = (RefSelectExpression) item;
//                if(!refExpression.getAlias().getName().contains("closed")) {
//                    newSelectItems.add(item);
//                }
            }
        }
        finalPlainSelect.getSelectItems().clear();
        finalPlainSelect.getSelectItems().addAll(newSelectItems);
    }
}
