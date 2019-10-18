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

package org.vgu.ocl2psql.ocl.roberts.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.ocl2psql.ocl.roberts.deparser.OclExpressionDeParser;
import org.vgu.ocl2psql.ocl.roberts.exception.OclConformanceException;
import org.vgu.ocl2psql.ocl.roberts.exception.SetOfSetException;
import org.vgu.ocl2psql.ocl.roberts.expressions.BooleanLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.CollectionLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.EnumLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IfExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IntegerLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.InvalidLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IterateExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorSource;
import org.vgu.ocl2psql.ocl.roberts.expressions.LetExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.MyIteratorSource;
import org.vgu.ocl2psql.ocl.roberts.expressions.NullLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.roberts.expressions.OperationCallExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.PropertyCallExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.RealLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.StringLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.TupleLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.TypeExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.Variable;
import org.vgu.ocl2psql.ocl.roberts.expressions.VariableExp;
import org.vgu.ocl2psql.ocl.roberts.utils.UMLContextUtils;
import org.vgu.ocl2psql.ocl.roberts.utils.VariableUtils;
import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;
import org.vgu.ocl2psql.ocl.type.SingleType;
import org.vgu.ocl2psql.ocl.type.Type;
import org.vgu.ocl2psql.sql.statement.select.Join;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.RefSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class RobertOCLParser implements RobertStmVisitor {
    private List<IteratorSource> context = new LinkedList<IteratorSource>();
    private JSONArray plainUMLContext;
    private int levelOfSets = 0;
    private Select finalSelect = new Select();
    private OclExpressionDeParser deParser = new OclExpressionDeParser();
    private MyIteratorSource self;
    private Type contextualType;
    
    public RobertOCLParser() {
        createSelfVariable();
        context.add(self);
    }

    private void createSelfVariable() {
        self = new MyIteratorSource();
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        ResSelectExpression item = new ResSelectExpression();
        item.setExpression(new Column("self"));
        finalPlainSelect.setRes(item);
        RefSelectExpression ref = new RefSelectExpression("self");
        ref.setExpression(new Column("self"));
        finalPlainSelect.addSelectItems(ref);
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);
        self.setSource(finalSelect);
        self.setIterator(new Variable("self"));
        setContextualTypeToSelf();
    }
    
    public List<IteratorSource> getVisitorContext() {
        return this.context;
    }

    public void setVisitorContext(List<IteratorSource> context) {
        this.context = context;
    }

    public JSONArray getPlainUMLContext() {
        return plainUMLContext;
    }

    public int getLevelOfSets() {
        return levelOfSets;
    }

    public void increaseLevelOfSet() {
        levelOfSets++;
    }

    public void decreaseLevelOfSet() {
        levelOfSets--;
    }

    public void resetLevelOfSet() {
        levelOfSets = 0;
    }

    public void setPlainUMLContextFromFile(String filePath) throws FileNotFoundException, IOException, ParseException {
        this.plainUMLContext = (JSONArray) new JSONParser().parse(new FileReader(filePath));
    }

    public void setPlainUMLContextFromString(String umlContext) throws ParseException {
        this.plainUMLContext = (JSONArray) new JSONParser().parse(umlContext);
    }

    public void setPlainUMLContext(JSONArray plainUMLContext) {
        this.plainUMLContext = plainUMLContext;
    }

    @Override
    public void visit(PropertyCallExp propertyCallExp) {
        Variable currentVariable = VariableUtils.getCurrentVariable(propertyCallExp);
        SubSelect tempVar = new SubSelect();
        propertyCallExp.getSource().accept(this);
        tempVar.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasSubSelectCurrentVar = new Alias("TEMP_obj");
        tempVar.setAlias(aliasSubSelectCurrentVar);
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(propertyCallExp, finalPlainSelect);
        String propertyName = propertyCallExp.getName().substring(propertyCallExp.getName().indexOf(":") + 1,
                propertyCallExp.getName().length());
        String propertyClass = propertyCallExp.getName().substring(0, propertyCallExp.getName().indexOf(":"));

        Type varType = propertyCallExp.getSource().getType();

        if (!UMLContextUtils.isSuperClassOf(this.getPlainUMLContext(), propertyClass,
                Optional.ofNullable(varType).map(Type::getTypeName).orElse(null))) {
            throw new OclConformanceException("Invalid operation on type ".concat(varType.getTypeName()));
        }

        ResSelectExpression resSelectExpression = new ResSelectExpression();
        resSelectExpression.setExpression(new Column(propertyName));

        Table table = new Table();

        if (UMLContextUtils.isAttribute(this.getPlainUMLContext(), propertyClass, propertyName)) {

            String tableRefColumn = propertyClass.concat(".").concat(propertyClass).concat("_id");

            String propertyType = UMLContextUtils.getAttributeType(this.getPlainUMLContext(), propertyClass,
                    propertyName);
            propertyCallExp.setType(new SingleType(propertyType));
            finalPlainSelect.setType(new TypeSelectExpression(propertyCallExp));

            table.setName(propertyClass);

            if (VariableUtils.isSourceAClassAllInstances(tempVar.getSelectBody(), propertyClass)) {
                finalPlainSelect.setRes(resSelectExpression);
                finalPlainSelect.setFromItem(table);
                VarSelectExpression newVar = new VarSelectExpression(currentVariable.getName());
                newVar.setRefExpression(new Column(table.getName().concat("_id")));
                finalPlainSelect.addVar(newVar);
            } else {
                BinaryExpression bexpr = new EqualsTo();
                bexpr.setLeftExpression(new Column(tableRefColumn));
                bexpr.setRightExpression(new Column(
                        aliasSubSelectCurrentVar.getName().concat(".ref_").concat(currentVariable.getName())));

                BinaryExpression varEx = new EqualsTo();
                varEx.setLeftExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".val")));
                varEx.setRightExpression(new LongValue(1L));

                AndExpression andEx = new AndExpression(bexpr, varEx);

                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(table);
                join.setOnExpression(andEx);
                finalPlainSelect.addSelectItems(resSelectExpression);
                finalPlainSelect
                        .setVal(new ValSelectExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".val"))));
                finalPlainSelect.setFromItem(tempVar);
                finalPlainSelect.setJoins(Arrays.asList(join));

                List<String> SVarsSource = VariableUtils.SVars(propertyCallExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
            }

            finalSelect.setSelectBody(finalPlainSelect);
            return;

        } else if (UMLContextUtils.isAssociation(this.getPlainUMLContext(), propertyClass, propertyName)) {
            String assocClass = UMLContextUtils.getAssociation(this.getPlainUMLContext(), propertyClass, propertyName);
            String oppositeEnd = UMLContextUtils.getAssociationOpposite(this.getPlainUMLContext(), propertyClass,
                    propertyName);

            String oppositeClassName = UMLContextUtils.getAssociationOppositeClassName(this.getPlainUMLContext(),
                    assocClass, propertyClass);
            //TODO: This is working because we flatten every single time.
            propertyCallExp.setType(new SingleType(oppositeClassName));

            table.setName(assocClass);

            if (VariableUtils.isSourceAClassAllInstances(tempVar.getSelectBody(), propertyClass)) {
                String tableName = ((Table) ((PlainSelect) tempVar.getSelectBody()).getFromItem()).getName();
                finalPlainSelect.setRes(resSelectExpression);
                finalPlainSelect.setFromItem(new Table(tableName));

                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(table);
                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(tableName.concat("_id")));
                onCondition.setRightExpression(new Column(assocClass.concat(".").concat(oppositeEnd)));
                join.setOnExpression(onCondition);
                finalPlainSelect.setJoins(Arrays.asList(join));

                CaseExpression caseValExpression = new CaseExpression();
                IsNullExpression isOppEndNull = new IsNullExpression();
                isOppEndNull.setLeftExpression(new Column(assocClass.concat(".").concat(oppositeEnd)));
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(isOppEndNull);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(isOppEndNull);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(propertyCallExp.getType().getTypeName()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                VarSelectExpression newVar = new VarSelectExpression(currentVariable.getName());
                newVar.setRefExpression(new Column(tableName.concat("_id")));
                finalPlainSelect.addVar(newVar);
            }

            else {
                BinaryExpression bexpr = new EqualsTo();
                bexpr.setLeftExpression(new Column(assocClass.concat(".").concat(oppositeEnd)));
                bexpr.setRightExpression(new Column(
                        aliasSubSelectCurrentVar.getName().concat(".ref_").concat(currentVariable.getName())));

                BinaryExpression varEx = new EqualsTo();
                varEx.setLeftExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".val")));
                varEx.setRightExpression(new LongValue(1L));

                AndExpression andEx = new AndExpression(bexpr, varEx);

                CaseExpression caseValExpression = new CaseExpression();
                IsNullExpression isOppEndNull = new IsNullExpression();
                isOppEndNull.setLeftExpression(new Column(assocClass.concat(".").concat(oppositeEnd)));
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(isOppEndNull);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(isOppEndNull);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(propertyCallExp.getType().getTypeName()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(table);
                join.setOnExpression(andEx);
                finalPlainSelect.addSelectItems(resSelectExpression);
                finalPlainSelect
                        .setVal(new ValSelectExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".val"))));
                finalPlainSelect.setFromItem(tempVar);
                finalPlainSelect.setJoins(Arrays.asList(join));

                List<String> SVarsSource = VariableUtils.SVars(propertyCallExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasSubSelectCurrentVar.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }

                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));
                this.increaseLevelOfSet();
            }

            finalSelect.setSelectBody(finalPlainSelect);
            return;
        } else {
            if (!UMLContextUtils.isClass(this.getPlainUMLContext(), propertyClass)) {
                throw new NullPointerException("Invalid class: ".concat(propertyClass));
            }
            throw new NullPointerException("Invalid attribute or association: ".concat(propertyName));
        }
    }

    private void genSQLCommentFromOCLExpressionToStatement(OclExpression oclExpression, PlainSelect finalPlainSelect) {
        deParser.clearComment();
        oclExpression.accept(deParser);
        finalPlainSelect.setCorrespondOCLExpression(deParser.getDeParsedStr());
    }

    @Override
    public void visit(OperationCallExp operationCallExp) {
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(operationCallExp, finalPlainSelect);

        if ("allInstances".equals(operationCallExp.getName())) {
            this.increaseLevelOfSet();
            String tableName = ((TypeExp) operationCallExp.getSource()).getReferredType();
            if (!UMLContextUtils.isClass(this.getPlainUMLContext(), tableName)) {
                throw new NullPointerException("Invalid class: ".concat(tableName));
            }
            operationCallExp.setType(new SingleType(tableName));
            ResSelectExpression resExpression = new ResSelectExpression(new Column(tableName.concat("_id")));
            Table table = new Table(tableName);
            finalPlainSelect.setRes(resExpression);
            finalPlainSelect.setType(new TypeSelectExpression(operationCallExp));
            finalPlainSelect.setFromItem(table);
        } else if ("not".equals(operationCallExp.getName())) {
            operationCallExp.setType(new SingleType("Boolean"));
            operationCallExp.getArguments().get(0).accept(this);
            PlainSelect selectBody = (PlainSelect) this.getFinalSelect().getSelectBody();
            ResSelectExpression curRes = selectBody.getRes();
            curRes.setExpression(new NotExpression(curRes.getExpression()));
        } else if ("oclIsUndefined".equals(operationCallExp.getName())) {
            operationCallExp.setType(new SingleType("Boolean"));
            operationCallExp.getSource().accept(this);
            Select select = this.getFinalSelect();
            SubSelect tempSource = new SubSelect();
            tempSource.setSelectBody(select.getSelectBody());
            Alias aliasSource = new Alias("TEMP_src");
            tempSource.setAlias(aliasSource);

            finalPlainSelect.setFromItem(tempSource);

            BinaryExpression valEq = new EqualsTo();
            valEq.setLeftExpression(new Column(aliasSource.getName().concat(".val")));
            valEq.setRightExpression(new LongValue(0L));

            CaseExpression caseResExpression = new CaseExpression();
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(valEq);
            whenResClause.setThenExpression(new NullValue());
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));

            IsNullExpression isNullExpr = new IsNullExpression();
            isNullExpr.setLeftExpression(new Column(aliasSource.getName().concat(".res")));
            caseResExpression.setElseExpression(isNullExpr);

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasSource.getName().concat(".val"))));

            finalPlainSelect.setType(new TypeSelectExpression(operationCallExp));

            List<String> sVarsSource = VariableUtils.SVars(operationCallExp.getSource(), this);
            for (String s : sVarsSource) {
                VarSelectExpression varExp = new VarSelectExpression(s);
                varExp.setRefExpression(new Column(aliasSource.getName().concat(".ref_").concat(s)));
                finalPlainSelect.addVar(varExp);
            }
        } else if ("oclIsTypeOf".equals(operationCallExp.getName())) {
            String classType = ((VariableExp) operationCallExp.getArguments().get(0)).getReferredVariable().getName();
            operationCallExp.setType(new SingleType("Boolean"));
            operationCallExp.getSource().accept(this);
            Select select = this.getFinalSelect();
            SubSelect tempSource = new SubSelect();
            tempSource.setSelectBody(select.getSelectBody());
            Alias aliasSource = new Alias("TEMP_src");
            tempSource.setAlias(aliasSource);

            finalPlainSelect.setFromItem(tempSource);

            BinaryExpression valEq = new EqualsTo();
            valEq.setLeftExpression(new Column(aliasSource.getName().concat(".val")));
            valEq.setRightExpression(new LongValue(0L));

            CaseExpression caseResExpression = new CaseExpression();
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(valEq);
            whenResClause.setThenExpression(new NullValue());
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));

            BinaryExpression typeEq = new EqualsTo();
            typeEq.setLeftExpression(new Column(aliasSource.getName().concat(".type")));
            typeEq.setRightExpression(new StringValue(classType));
            caseResExpression.setElseExpression(typeEq);

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasSource.getName().concat(".val"))));

            finalPlainSelect.setType(new TypeSelectExpression(operationCallExp));

            List<String> sVarsSource = VariableUtils.SVars(operationCallExp.getSource(), this);
            for (String s : sVarsSource) {
                VarSelectExpression varExp = new VarSelectExpression(s);
                varExp.setRefExpression(new Column(aliasSource.getName().concat(".ref_").concat(s)));
                finalPlainSelect.addVar(varExp);
            }
        } else if ("oclIsKindOf".equals(operationCallExp.getName())) {
            String classType = ((VariableExp) operationCallExp.getArguments().get(0)).getReferredVariable().getName();
            operationCallExp.setType(new SingleType("Boolean"));
            operationCallExp.getSource().accept(this);
            ;
            Select select = this.getFinalSelect();
            SubSelect tempSource = new SubSelect();
            tempSource.setSelectBody(select.getSelectBody());
            Alias aliasSource = new Alias("TEMP_src");
            tempSource.setAlias(aliasSource);

            Type sourceType = operationCallExp.getSource().getType();
            boolean isKindOf = UMLContextUtils.isSuperClassOf(this.getPlainUMLContext(), classType,
                    Optional.ofNullable(sourceType).map(Type::getTypeName).orElse(null));

            finalPlainSelect.setFromItem(tempSource);

            BinaryExpression valEq = new EqualsTo();
            valEq.setLeftExpression(new Column(aliasSource.getName().concat(".val")));
            valEq.setRightExpression(new LongValue(0L));

            CaseExpression caseResExpression = new CaseExpression();
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(valEq);
            whenResClause.setThenExpression(new NullValue());
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));

            caseResExpression.setElseExpression(new LongValue(isKindOf ? "TRUE" : "FALSE"));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasSource.getName().concat(".val"))));

            finalPlainSelect.setType(new TypeSelectExpression(operationCallExp));

            List<String> sVarsSource = VariableUtils.SVars(operationCallExp.getSource(), this);
            for (String s : sVarsSource) {
                VarSelectExpression varExp = new VarSelectExpression(s);
                varExp.setRefExpression(new Column(aliasSource.getName().concat(".ref_").concat(s)));
                finalPlainSelect.addVar(varExp);
            }
        } else if ("oclAsType".equals(operationCallExp.getName())) {
            String classType = ((VariableExp) operationCallExp.getArguments().get(0)).getReferredVariable().getName();
            operationCallExp.getSource().accept(this);
            Select select = this.getFinalSelect();
            SubSelect tempSource = new SubSelect();
            tempSource.setSelectBody(select.getSelectBody());
            Alias aliasSource = new Alias("TEMP_src");
            tempSource.setAlias(aliasSource);

            Type sourceType = operationCallExp.getSource().getType();
            boolean isKindOf = UMLContextUtils.isSuperClassOf(this.getPlainUMLContext(), sourceType.getTypeName(),
                    classType);

            if (!isKindOf) {
                throw new OclConformanceException(
                        sourceType.getTypeName().concat(" does not conform to ").concat(classType));
            } else {
                operationCallExp.setType(new SingleType(classType));
                finalPlainSelect.setFromItem(tempSource);

                BinaryExpression valEq = new EqualsTo();
                valEq.setLeftExpression(new Column(aliasSource.getName().concat(".val")));
                valEq.setRightExpression(new LongValue(0L));

                finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasSource.getName().concat(".res"))));

                finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasSource.getName().concat(".val"))));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(valEq);
                whenTypeClause.setThenExpression(new NullValue());
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(classType));

                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                if (!(classType.equals("String") || classType.equals("Integer") || classType.equals("Boolean")
                        || classType.equals(sourceType.getTypeName()))) {
                    Join join = new Join();
                    join.setRightItem(new Table(classType));
//                    join.setSemi(true);
//                    join.setUsingColumns(Arrays.asList(new Column(classType.concat("_id"))));
                    BinaryExpression idEqual = new EqualsTo();
                    idEqual.setLeftExpression(new Column(aliasSource.getName().concat(".res")));
                    idEqual.setRightExpression(new Column(classType.concat("_id")));
                    join.setOnExpression(idEqual);
                    finalPlainSelect.setJoins(Arrays.asList(join));
                }

                List<String> sVarsSource = VariableUtils.SVars(operationCallExp.getSource(), this);
                for (String s : sVarsSource) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(aliasSource.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }
            }
        } else {
            operationCallExp.setType(new SingleType("Boolean"));
            SubSelect tempLeft = new SubSelect();
            operationCallExp.getSource().accept(this);
            tempLeft.setSelectBody(this.getFinalSelect().getSelectBody());
            Alias alias_Left = new Alias("TEMP_LEFT");
            tempLeft.setAlias(alias_Left);

            SubSelect tempRight = new SubSelect();
            operationCallExp.getArguments().get(0).accept(this);
            tempRight.setSelectBody(this.getFinalSelect().getSelectBody());
            Alias alias_Right = new Alias("TEMP_RIGHT");
            tempRight.setAlias(alias_Right);

            List<String> fVarsLeft = VariableUtils.FVars(operationCallExp.getSource());
            List<String> fVarsRight = VariableUtils.FVars(operationCallExp.getArguments().get(0));
            List<String> sVarsLeft = VariableUtils.SVars(operationCallExp.getSource(), this);
            List<String> sVarsRight = VariableUtils.SVars(operationCallExp.getArguments().get(0), this);

            finalPlainSelect.setType(new TypeSelectExpression(operationCallExp));

            if (fVarsLeft.isEmpty() && fVarsRight.isEmpty()) {
                ResSelectExpression resExp = new ResSelectExpression();
                BinaryExpression eqExp = generateBinaryExpression(operationCallExp.getName(),
                        new Column(alias_Left.getName().concat(".res")),
                        new Column(alias_Right.getName().concat(".res")));
                resExp.setExpression(eqExp);
                finalPlainSelect.setRes(resExp);
                finalPlainSelect.setFromItem(tempLeft);
                finalPlainSelect.createTrueValColumn();
                Join join = new Join();
                join.setSimple(true);
                join.setRightItem(tempRight);
                finalPlainSelect.setJoins(Arrays.asList(join));
            } else if (!fVarsLeft.isEmpty() && sVarsRight.stream().allMatch(vr -> sVarsLeft.contains(vr))) {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);

                BinaryExpression eqExp = generateBinaryExpression(operationCallExp.getName(),
                        new Column(alias_Left.getName().concat(".res")),
                        new Column(alias_Right.getName().concat(".res")));

                finalPlainSelect.setRes(new ResSelectExpression(eqExp));

                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(orExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                for (String s : sVarsLeft) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Left.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }

                finalPlainSelect.setFromItem(tempLeft);

                Join join = new Join();
                join.setRightItem(tempRight);
                finalPlainSelect.setJoins(Arrays.asList(join));
                List<String> sVarsIntercept = new ArrayList<String>();
                for (String sr : sVarsRight) {
                    if (sVarsLeft.contains(sr))
                        sVarsIntercept.add(sr);
                }
                if (!sVarsIntercept.isEmpty()) {
                    join.setRight(true);
                    BinaryExpression onExp = null;
                    for (String v : sVarsIntercept) {
                        if (Objects.isNull(onExp)) {
                            onExp = new EqualsTo();
                            onExp.setLeftExpression(new Column(alias_Left.getName().concat(".ref_").concat(v)));
                            onExp.setRightExpression(new Column(alias_Right.getName().concat(".ref_").concat(v)));
                        } else {
                            BinaryExpression holderExp = new EqualsTo();
                            holderExp.setLeftExpression(new Column(alias_Left.getName().concat(".ref_").concat(v)));
                            holderExp.setRightExpression(new Column(alias_Right.getName().concat(".ref_").concat(v)));
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }
            } else if (!fVarsRight.isEmpty() && sVarsLeft.stream().allMatch(vl -> sVarsRight.contains(vl))) {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);

                BinaryExpression eqExp = generateBinaryExpression(operationCallExp.getName(),
                        new Column(alias_Left.getName().concat(".res")),
                        new Column(alias_Right.getName().concat(".res")));

                finalPlainSelect.setRes(new ResSelectExpression(eqExp));

                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(orExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                for (String s : sVarsRight) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Right.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }

                finalPlainSelect.setFromItem(tempRight);

                Join join = new Join();
                join.setRightItem(tempLeft);
                finalPlainSelect.setJoins(Arrays.asList(join));
                List<String> sVarsIntercept = new ArrayList<String>();
                for (String sl : sVarsLeft) {
                    if (sVarsRight.contains(sl))
                        sVarsIntercept.add(sl);
                }
                if (!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for (String v : sVarsIntercept) {
                        if (Objects.isNull(onExp)) {
                            onExp = new EqualsTo();
                            onExp.setLeftExpression(new Column(alias_Left.getName().concat(".ref_").concat(v)));
                            onExp.setRightExpression(new Column(alias_Right.getName().concat(".ref_").concat(v)));
                        } else {
                            BinaryExpression holderExp = new EqualsTo();
                            holderExp.setLeftExpression(new Column(alias_Left.getName().concat(".ref_").concat(v)));
                            holderExp.setRightExpression(new Column(alias_Right.getName().concat(".ref_").concat(v)));
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }
            } else {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);
                BinaryExpression eqExp = generateBinaryExpression(operationCallExp.getName(),
                        new Column(alias_Left.getName().concat(".res")),
                        new Column(alias_Right.getName().concat(".res")));

                finalPlainSelect.setRes(new ResSelectExpression(eqExp));

                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(orExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                for (String s : sVarsLeft) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Left.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }
                for (String s : sVarsRight) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Right.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }

                finalPlainSelect.setFromItem(tempLeft);

                Join join = new Join();
                join.setSimple(true);
                join.setRightItem(tempRight);
                finalPlainSelect.setJoins(Arrays.asList(join));
            }
        }

        finalSelect.setSelectBody(finalPlainSelect);
        return;
    }

    private BinaryExpression generateBinaryExpression(String operation, Column leftExpression, Column rightExpression) {

        BinaryExpression binaryExpr = null;

        switch (operation) {
        case "=":
            binaryExpr = new EqualsTo();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case "<>":
            binaryExpr = new NotEqualsTo();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case ">":
            binaryExpr = new GreaterThan();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case "<":
            binaryExpr = new MinorThan();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case ">=":
            binaryExpr = new GreaterThanEquals();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case "<=":
            binaryExpr = new MinorThanEquals();
            binaryExpr.setLeftExpression(leftExpression);
            binaryExpr.setRightExpression(rightExpression);
            return binaryExpr;
        case "and":
            return new AndExpression(leftExpression, rightExpression);
        case "or":
            return new OrExpression(leftExpression, rightExpression);
        default:
            return null;
        }
    }

    @Override
    public void visit(IterateExp iterateExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IteratorExp iteratorExp) {
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(iteratorExp, finalPlainSelect);

        switch (iteratorExp.kind) {
        case isEmpty:
            emptyMap(iteratorExp, finalPlainSelect);
            break;
        case notEmpty:
            notEmptyMap(iteratorExp, finalPlainSelect);
            break;
        case select:
            selectMap(iteratorExp, finalPlainSelect);
            break;
        case reject:
            rejectMap(iteratorExp, finalPlainSelect);
            break;
        case forAll:
            forAllMap(iteratorExp, finalPlainSelect);
            break;
        case exists:
            existsMap(iteratorExp, finalPlainSelect);
            break;
        case collect:
            collectMap(iteratorExp, finalPlainSelect);
            break;
        case size:
            sizeMap(iteratorExp, finalPlainSelect);
            break;
        case asSet:
            asSetMap(iteratorExp, finalPlainSelect);
            break;
        case isUnique:
            isUniqueMap(iteratorExp, finalPlainSelect);
            break;
        case flatten:
            flattenMap(iteratorExp, finalPlainSelect);
            break;
        case sum:
            throw new NullPointerException("Unsupported sum operation");
        case asBag:
            throw new NullPointerException("Unsupported asBag operation");
        case asOrderedSet:
            throw new NullPointerException("Unsupported asOrderedSet operation");
        case asSequence:
            throw new NullPointerException("Unsupported asSequence operation");
        case at:
            throw new NullPointerException("Unsupported at operation");
        case indexOf:
            throw new NullPointerException("Unsupported indexOf operation");
        case count:
            throw new NullPointerException("Unsupported count operation");
        case first:
            throw new NullPointerException("Unsupported first operation");
        case last:
            throw new NullPointerException("Unsupported last operation");
        case including:
            throw new NullPointerException("Unsupported including operation");
        case excluding:
            throw new NullPointerException("Unsupported excluding operation");
        case includes:
            throw new NullPointerException("Unsupported includes operation");
        case excludes:
            throw new NullPointerException("Unsupported excludes operation");
        case union:
            throw new NullPointerException("Unsupported union operation");
        case includesAll:
            throw new NullPointerException("Unsupported includesAll operation");
        case excludesAll:
            throw new NullPointerException("Unsupported excludesAll operation");
        case any:
            throw new NullPointerException("Unsupported any operation");
        case one:
            throw new NullPointerException("Unsupported one operation");
        case sortedBy:
            throw new NullPointerException("Unsupported sortedBy operation");
        default:
            return;
        }
    }

    @Override
    public void visit(IfExp ifExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LetExp letExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CollectionLiteralExp collectionLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EnumLiteralExp enumLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(InvalidLiteralExp invalidLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NullLiteralExp nullLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(BooleanLiteralExp booleanLiteralExp) {
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(booleanLiteralExp, finalPlainSelect);
        ResSelectExpression resExpression = new ResSelectExpression(
                new LongValue(booleanLiteralExp.isBooleanSymbol() ? "TRUE" : "FALSE"));
        finalPlainSelect.setRes(resExpression);
        finalPlainSelect.setType(new TypeSelectExpression(booleanLiteralExp));
        finalSelect.setSelectBody(finalPlainSelect);
    }

    @Override
    public void visit(IntegerLiteralExp integerLiteralExp) {
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(integerLiteralExp, finalPlainSelect);
        ResSelectExpression resExpression = new ResSelectExpression(
                new LongValue(integerLiteralExp.getIntegerSymbol()));
        finalPlainSelect.setRes(resExpression);
        finalPlainSelect.setType(new TypeSelectExpression(integerLiteralExp));
        finalSelect.setSelectBody(finalPlainSelect);
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringLiteralExp stringLiteralExp) {
        PlainSelect finalPlainSelect = new PlainSelect();
        finalPlainSelect.createTrueValColumn();
        genSQLCommentFromOCLExpressionToStatement(stringLiteralExp, finalPlainSelect);
        ResSelectExpression resExpression = new ResSelectExpression(
                new StringValue(stringLiteralExp.getStringSymbol()));
        finalPlainSelect.setRes(resExpression);
        finalPlainSelect.setType(new TypeSelectExpression(stringLiteralExp));
        finalSelect.setSelectBody(finalPlainSelect);
    }

    @Override
    public void visit(TupleLiteralExp tupleLiteralExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(TypeExp typeExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VariableExp variableExp) {
        String var_name = variableExp.getReferredVariable().getName();
        MyIteratorSource iter = null;
        for (IteratorSource iter_item : this.getVisitorContext()) {
            if (iter_item.getIterator().getName().equals(var_name)) {
                iter = (MyIteratorSource) iter_item;
                break;
            }
        }
        if (iter == null) {
            PlainSelect finalPlainSelect = new PlainSelect();
            finalPlainSelect.createTrueValColumn();
            ResSelectExpression item = new ResSelectExpression();
            item.setExpression(new Column(variableExp.getReferredVariable().getName()));
            finalPlainSelect.setRes(item);
            RefSelectExpression ref = new RefSelectExpression(variableExp.getReferredVariable().getName());
            ref.setExpression(new Column(variableExp.getReferredVariable().getName()));
            finalPlainSelect.addSelectItems(ref);
            variableExp.setType(null);
            finalPlainSelect.setType(new TypeSelectExpression(variableExp));
            finalSelect.setSelectBody(finalPlainSelect);
            // Create and add new iterator into visitor context.
            MyIteratorSource newFreeIteratorSource = new MyIteratorSource();
            newFreeIteratorSource.setIterator(new Variable(var_name));
            newFreeIteratorSource.setSource(finalSelect);
            this.getVisitorContext().add(newFreeIteratorSource);
            genSQLCommentFromOCLExpressionToStatement(variableExp, finalPlainSelect);
            return;
        } else {
            if("self".equals(var_name)) {
                variableExp.setType(this.contextualType);
            } else {
                variableExp.setType(iter.getSourceExpression().getElementType());
            }
            finalSelect.setSelectBody(iter.getSource().getSelectBody());
            PlainSelect finalPlainSelect = (PlainSelect) finalSelect.getSelectBody();
            genSQLCommentFromOCLExpressionToStatement(variableExp, finalPlainSelect);
            return;
        }
    }

    public Select getFinalSelect() {
        return finalSelect;
    }

    public void setFinalSelect(Select finalSelect) {
        this.finalSelect = finalSelect;
    }

    private void flattenMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        if (iteratorExp.getSource() instanceof IteratorExp) {
            iteratorExp.getSource().accept(this);
            Select source = this.getFinalSelect();

            SubSelect tempFlattenSource = new SubSelect();
            tempFlattenSource.setSelectBody(source.getSelectBody());

            List<String> sVarSource = VariableUtils.FVars(iteratorExp.getSource());

            if (sVarSource.isEmpty()) {

                Alias aliasTempFlattenSource = new Alias("TEMP_src");
                tempFlattenSource.setAlias(aliasTempFlattenSource);

                finalPlainSelect.setFromItem(tempFlattenSource);
                finalPlainSelect
                        .setRes(new ResSelectExpression(new Column(aliasTempFlattenSource.getName().concat(".res"))));
                finalPlainSelect.createTrueValColumn();
                finalPlainSelect.setType(new TypeSelectExpression(iteratorExp.getSource()));

                BinaryExpression valTrue = new EqualsTo();
                valTrue.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valTrue.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(valTrue);

            } else {
                Alias aliasTempFlattenSource = new Alias("TEMP");
                tempFlattenSource.setAlias(aliasTempFlattenSource);

                PlainSelect sCollectvB = new PlainSelect();
                sCollectvB.createTrueValColumn();
                sCollectvB.setAllColumn();
                sCollectvB.setFromItem(tempFlattenSource);

                BinaryExpression valIsOne = new EqualsTo();
                valIsOne.setLeftExpression(new Column(aliasTempFlattenSource.getName().concat(".val")));
                valIsOne.setRightExpression(new LongValue(1L));
                sCollectvB.setWhere(valIsOne);

                SubSelect tempFlat = new SubSelect(sCollectvB, "TEMP_flat");

                Alias aliasTempFlat = tempFlat.getAlias();

                Join join = new Join();
                join.setLeft(true);
                join.setRightItem(tempFlat);

                finalPlainSelect.setJoins(Arrays.asList(join));

                PlainSelect s = (PlainSelect) tempFlattenSource.getSelectBody();

                SubSelect tempCollectSource = new SubSelect(s, "TEMP_src");

                finalPlainSelect.setFromItem(tempCollectSource);

//             String flattenVar = ((IteratorExp) this.getSource()).getIterator().getName();

                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempFlat.getName().concat(".val")));

                CaseExpression caseResVExpression = new CaseExpression();
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new NullValue());
                caseResVExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".res")));

                CaseExpression caseValVExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(isOuterRefNull);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValVExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValVExpression.setElseExpression(new Column(aliasTempFlat.getName().concat(".val")));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(isOuterRefNull);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression.setElseExpression(new StringValue(iteratorExp.getSource().getType().getTypeName()));

                finalPlainSelect.setRes(new ResSelectExpression(caseResVExpression));
                finalPlainSelect.setVal(new ValSelectExpression(caseValVExpression));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                List<String> sVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                BinaryExpression onCondition = null;

                for (String v : sVarsSource) {
//                 if(v.equals(flattenVar)) continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);

                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(
                            new Column(tempCollectSource.getAlias().getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempFlat.getName().concat(".ref_").concat(v)));

                    if (Objects.isNull(onCondition)) {
                        onCondition = holderExp;
                    } else {
                        onCondition = new AndExpression(onCondition, holderExp);
                    }
                }
                join.setOnExpression(onCondition);

                iteratorExp.setType(iteratorExp.getSource().getType());

                this.decreaseLevelOfSet();

            }
            finalSelect.setSelectBody(finalPlainSelect);
            return;
        }
        throw new SetOfSetException("The source is not set of set to be flattened");
    }

    private void isUniqueMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.getSource().accept(this);
        Select sizeSourceSelect = this.getFinalSelect();
        PlainSelect sizeSourcePlainSelect = (PlainSelect) sizeSourceSelect.getSelectBody();

        SubSelect finalSubSelect = new SubSelect();
        finalSubSelect.setSelectBody(sizeSourcePlainSelect);
        Alias aliasFinalSubSelect = new Alias("TEMP_unique_source");
        finalSubSelect.setAlias(aliasFinalSubSelect);

        finalSelect.setSelectBody(finalPlainSelect);

        finalPlainSelect.setFromItem(finalSubSelect);
        ResSelectExpression uniqueRes = new ResSelectExpression();
        Function countAll = new Function();
        countAll.setName("COUNT");
        countAll.setParameters(
                new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        Function countAllDistinct = new Function();
        countAllDistinct.setName("COUNT");
        countAllDistinct.setDistinct(true);
        countAllDistinct.setParameters(
                new ExpressionList(Arrays.asList(new Column(aliasFinalSubSelect.getName().concat(".res")))));
        BinaryExpression eqEx = new EqualsTo();
        eqEx.setLeftExpression(countAll);
        eqEx.setRightExpression(countAllDistinct);
        uniqueRes.setExpression(eqEx);
        finalPlainSelect.setRes(uniqueRes);
        iteratorExp.setType(new SingleType("Boolean"));
        finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));
        return;
    }

    private void rejectMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(iteratorExp.getSource());
        currentIterator.setIterator(iteratorExp.getIterator());
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();
        currentIterator.setSource(source);

        SubSelect tempSelectSource = new SubSelect();
        tempSelectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempSelectSource = new Alias("TEMP_src");
        tempSelectSource.setAlias(aliasTempSelectSource);

        if (this.getVisitorContext().stream().map(IteratorSource::getIterator).map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            this.getVisitorContext().add(currentIterator);
        }

        SubSelect tempSelectBody = new SubSelect();
        iteratorExp.getBody().accept(this);
        tempSelectBody.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasTempSelectBody = new Alias("TEMP_body");
        tempSelectBody.setAlias(aliasTempSelectBody);

        String currentIter = iteratorExp.getIterator().getName();

        List<String> fVarsSource = VariableUtils.FVars(iteratorExp.getSource());
        List<String> fVarsBody = VariableUtils.FVars(iteratorExp.getBody());

        if (VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if (fVarsSource.isEmpty()) {
                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(0L));

                IsNullExpression isNullExpr = new IsNullExpression();
                isNullExpr.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));

                OrExpression orExpr = new OrExpression(bodyWhereExp, isNullExpr);

                gBody.setWhere(orExpr);

                finalPlainSelect.setRes(new ResSelectExpression(
                        new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempGBody.getName().concat(".val"))));
                finalPlainSelect.setType(new TypeSelectExpression(iteratorExp.getSource()));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }

                finalPlainSelect.setFromItem(tempGBody);
            } else {
                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                CaseExpression caseResExpression = new CaseExpression();
                BinaryExpression isValValid = new EqualsTo();
                isValValid.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".val")));
                isValValid.setRightExpression(new LongValue(0L));
                IsNullExpression isBodyRefNull = new IsNullExpression();
                isBodyRefNull
                        .setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                BinaryExpression caseBinExp = new OrExpression(isValValid, isBodyRefNull);
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(caseBinExp);
                whenResClause.setThenExpression(new NullValue());
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(caseBinExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(caseBinExp);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression
                        .setElseExpression(new StringValue(iteratorExp.getSource().getElementType().getTypeName()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                onCondition
                        .setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression binExp = new EqualsTo();
                    binExp.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    binExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    onCondition = new AndExpression(onCondition, binExp);
                }
                List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);
                for (String v : SVarsBody) {
                    if (v.equals(currentIter) || SVarsSource.contains(v))
                        continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);

                finalPlainSelect.setFromItem(tempSelectSource);
                Join join = new Join();

                join.setRightItem(tempGBody);
                finalPlainSelect.setJoins(Arrays.asList(join));

                join.setOnExpression(onCondition);
            }

        }

        iteratorExp.setType(iteratorExp.getSource().getType());
        // create result
        finalSelect.setSelectBody(finalPlainSelect);

        return;
    }

    private void asSetMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();

        SubSelect tempAsSetSource = new SubSelect(source.getSelectBody(), "TEMP_src");

        finalSelect.setSelectBody(finalPlainSelect);

        finalPlainSelect.setFromItem(tempAsSetSource);

        Distinct distinct = new Distinct();
        finalPlainSelect.setDistinct(distinct);

        VariableUtils.reserveVars(finalPlainSelect, tempAsSetSource);
        finalPlainSelect.setRes(new ResSelectExpression(new Column("TEMP_src.res")));
        finalPlainSelect.setVal(new ValSelectExpression(new Column("TEMP_src.val")));
        finalPlainSelect.setType(new TypeSelectExpression(new Column("TEMP_src.type")));

        iteratorExp.setType(iteratorExp.getSource().getType());
    }

    private void existsMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.setType(new SingleType("Boolean"));
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(iteratorExp.getSource());
        currentIterator.setIterator(iteratorExp.getIterator());
        iteratorExp.getSource().accept(this);
        Select source = new Select();
        source.setSelectBody(this.getFinalSelect().getSelectBody());
        currentIterator.setSource(source);

        SubSelect tempExistsSource = new SubSelect();
        tempExistsSource.setSelectBody(source.getSelectBody());
        Alias aliasTempExistsSource = new Alias("TEMP_src");
        tempExistsSource.setAlias(aliasTempExistsSource);
        finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));

        if (this.getVisitorContext().stream().map(IteratorSource::getIterator).map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            this.getVisitorContext().add(currentIterator);
        }
        SubSelect tempExistsBody = new SubSelect();
        iteratorExp.getBody().accept(this);
        tempExistsBody.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasTempExistsBody = new Alias("TEMP_body");
        tempExistsBody.setAlias(aliasTempExistsBody);

        String currentIter = iteratorExp.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars(iteratorExp.getSource());
        List<String> fVarsBody = VariableUtils.FVars(iteratorExp.getBody());

        if (VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if (fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.createTrueValColumn();

                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression greaterThanZero = new GreaterThan();
                greaterThanZero.setLeftExpression(count);
                greaterThanZero.setRightExpression(new LongValue(0L));

                finalPlainSelect.setRes(new ResSelectExpression(greaterThanZero));

                finalPlainSelect.setFromItem(tempExistsBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                finalPlainSelect.setWhere(bodyWhereExp);

            } else {
                finalPlainSelect.createTrueValColumn();

                String outerVar = VariableUtils.getOuterVariable(iteratorExp);
                MyIteratorSource outerIter = this.getVisitorContext().stream()
                        .filter(v -> v.getIterator().getName().equals(outerVar)).findFirst()
                        .map(MyIteratorSource.class::cast).get();

                SubSelect tempVar = new SubSelect();
                tempVar.setSelectBody(outerIter.getSource().getSelectBody());
                Alias aliasTempVar = new Alias("TEMP_src");
                tempVar.setAlias(aliasTempVar);

                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempExistsBody);
                gBody.getSelectItems().clear();

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);

                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression greaterThanZero = new GreaterThan();
                greaterThanZero.setLeftExpression(count);
                greaterThanZero.setRightExpression(new LongValue(0L));
                gBody.setRes(new ResSelectExpression(greaterThanZero));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                }

                GroupByElement groupByElement = new GroupByElement();
                groupByElement.setGroupByExpressions(groupByExpressions);
                gBody.setGroupByElement(groupByElement);

                List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);

                finalPlainSelect.setFromItem(tempVar);
                Join join = new Join();
                join.setRightItem(tempGBody);

                List<String> sVarsIntercept = new ArrayList<String>();
                for (String sr : SVarsBody) {
                    if (SVarsSource.contains(sr))
                        sVarsIntercept.add(sr);
                }
                if (!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for (String v : sVarsIntercept) {
                        BinaryExpression holderExp = new EqualsTo();
                        holderExp.setLeftExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        holderExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                        VarSelectExpression newVar = new VarSelectExpression(v);
                        newVar.setRefExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        finalPlainSelect.addVar(newVar);
                        if (Objects.isNull(onExp)) {
                            onExp = holderExp;
                        } else {
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }

                finalPlainSelect.setJoins(Arrays.asList(join));

                CaseExpression caseResExpression = new CaseExpression();
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(outerVar)));
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new LongValue(0L));
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempGBody.getName().concat(".res")));

                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            }
            finalSelect.setSelectBody(finalPlainSelect);
        } else {
            finalPlainSelect.createTrueValColumn();

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
            for (String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
            List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);

            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempExistsBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(1L));
            finalPlainSelect.setWhere(bodyWhereExp);

            finalPlainSelect.setFromItem(tempExistsSource);
            Join join = new Join();
            join.setRightItem(tempExistsBody);

            BinaryExpression onExp = null;

            List<String> sVarsIntercept = new ArrayList<String>();
            for (String sr : SVarsBody) {
                if (SVarsSource.contains(sr))
                    sVarsIntercept.add(sr);
            }
            if (!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                for (String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempExistsBody.getName().concat(".ref_").concat(v)));
                    if (Objects.isNull(onExp)) {
                        onExp = holderExp;
                    } else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
            }

            finalPlainSelect.setJoins(Arrays.asList(join));

            CaseExpression caseResExpression = new CaseExpression();
            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempExistsSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            BinaryExpression greaterThanZero = new GreaterThan();
            greaterThanZero.setLeftExpression(count);
            greaterThanZero.setRightExpression(new LongValue(0L));
            caseResExpression.setElseExpression(greaterThanZero);
            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            finalSelect.setSelectBody(finalPlainSelect);
        }
    }

    private void forAllMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.setType(new SingleType("Boolean"));
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(iteratorExp.getSource());
        currentIterator.setIterator(iteratorExp.getIterator());
        iteratorExp.getSource().accept(this);
        Select source = new Select();
        source.setSelectBody(this.getFinalSelect().getSelectBody());
        currentIterator.setSource(source);

        SubSelect tempForAllSource = new SubSelect();
        tempForAllSource.setSelectBody(source.getSelectBody());
        Alias aliasTempForAllSource = new Alias("TEMP_src");
        tempForAllSource.setAlias(aliasTempForAllSource);

        if (this.getVisitorContext().stream().map(IteratorSource::getIterator).map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            this.getVisitorContext().add(currentIterator);
        }
        SubSelect tempForAllBody = new SubSelect();
        iteratorExp.getBody().accept(this);
        tempForAllBody.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasTempForAllBody = new Alias("TEMP_body");
        tempForAllBody.setAlias(aliasTempForAllBody);

        String currentIter = iteratorExp.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars(iteratorExp.getSource());
        List<String> fVarsBody = VariableUtils.FVars(iteratorExp.getBody());
        finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));

        if (VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if (fVarsSource.isEmpty() && fVarsBody.size() == 1) {
                finalPlainSelect.createTrueValColumn();

                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression eqZero = new EqualsTo();
                eqZero.setLeftExpression(count);
                eqZero.setRightExpression(new LongValue(0L));
                finalPlainSelect.setRes(new ResSelectExpression(eqZero));

                finalPlainSelect.setFromItem(tempForAllBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(0L));
                finalPlainSelect.setWhere(bodyWhereExp);
            } else {
                finalPlainSelect.createTrueValColumn();

                String outerVar = VariableUtils.getOuterVariable(iteratorExp);
                MyIteratorSource outerIter = this.getVisitorContext().stream()
                        .filter(v -> v.getIterator().getName().equals(outerVar)).findFirst()
                        .map(MyIteratorSource.class::cast).get();

                SubSelect tempVar = new SubSelect();
                tempVar.setSelectBody(outerIter.getSource().getSelectBody());
                Alias aliasTempVar = new Alias("TEMP_src");
                tempVar.setAlias(aliasTempVar);

                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempForAllBody);

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".val")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                Function ifNull = new Function();
                ifNull.setName("IFNULL");
                ifNull.setParameters(new ExpressionList(new Column(aliasTempForAllBody.getName().concat(".res")),
                        new LongValue(0L)));
                BinaryExpression isNullOrZero = new EqualsTo();
                isNullOrZero.setLeftExpression(ifNull);
                isNullOrZero.setRightExpression(new LongValue(0L));
                gBody.setWhere(new AndExpression(bodyWhereExp, isNullOrZero));

                Function count = new Function();
                count.setName("COUNT");
                count.setAllColumns(true);
                BinaryExpression eqZero = new EqualsTo();
                eqZero.setLeftExpression(count);
                eqZero.setRightExpression(new LongValue(0L));
                gBody.setRes(new ResSelectExpression(eqZero));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                List<Expression> groupByExpressions = new ArrayList<Expression>();
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                    gBody.addVar(newVar);
                    groupByExpressions.add(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                }
                GroupByElement groupByElement = new GroupByElement();
                groupByElement.setGroupByExpressions(groupByExpressions);
                gBody.setGroupByElement(groupByElement);

                List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);

                finalPlainSelect.setFromItem(tempVar);
                Join join = new Join();
                join.setRightItem(tempGBody);

                List<String> sVarsIntercept = new ArrayList<String>();
                for (String sr : SVarsBody) {
                    if (SVarsSource.contains(sr))
                        sVarsIntercept.add(sr);
                }
                if (!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for (String v : sVarsIntercept) {
                        BinaryExpression holderExp = new EqualsTo();
                        holderExp.setLeftExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        holderExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                        VarSelectExpression newVar = new VarSelectExpression(v);
                        newVar.setRefExpression(new Column(aliasTempVar.getName().concat(".ref_").concat(v)));
                        finalPlainSelect.addVar(newVar);
                        if (Objects.isNull(onExp)) {
                            onExp = holderExp;
                        } else {
                            onExp = new AndExpression(onExp, holderExp);
                        }
                    }
                    join.setOnExpression(onExp);
                }

                finalPlainSelect.setJoins(Arrays.asList(join));

                CaseExpression caseResExpression = new CaseExpression();
                IsNullExpression isOuterRefNull = new IsNullExpression();
                isOuterRefNull.setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(outerVar)));
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(isOuterRefNull);
                whenResClause.setThenExpression(new LongValue(1L));
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempGBody.getName().concat(".res")));

                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            }
            finalSelect.setSelectBody(finalPlainSelect);
        } else {
            finalPlainSelect.createTrueValColumn();

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
            for (String v : SVarsSource) {
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempForAllSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
            List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);

            BinaryExpression bodyWhereExp = new EqualsTo();
            bodyWhereExp.setLeftExpression(new Column(aliasTempForAllBody.getName().concat(".res")));
            bodyWhereExp.setRightExpression(new LongValue(0L));
            finalPlainSelect.setWhere(bodyWhereExp);

            finalPlainSelect.setFromItem(tempForAllSource);
            Join join = new Join();
            join.setRightItem(tempForAllBody);

            BinaryExpression onExp = null;

            List<String> sVarsIntercept = new ArrayList<String>();
            for (String sr : SVarsBody) {
                if (SVarsSource.contains(sr))
                    sVarsIntercept.add(sr);
            }
            if (!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                for (String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    holderExp.setLeftExpression(new Column(aliasTempForAllSource.getName().concat(".ref_").concat(v)));
                    holderExp.setRightExpression(new Column(aliasTempForAllBody.getName().concat(".ref_").concat(v)));
                    if (Objects.isNull(onExp)) {
                        onExp = holderExp;
                    } else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
            }

            finalPlainSelect.setJoins(Arrays.asList(join));

            CaseExpression caseResExpression = new CaseExpression();
            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempForAllSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));
            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(1L));
            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            BinaryExpression eqZero = new EqualsTo();
            eqZero.setLeftExpression(count);
            eqZero.setRightExpression(new LongValue(0L));
            caseResExpression.setElseExpression(eqZero);
            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

            finalSelect.setSelectBody(finalPlainSelect);
        }
    }

    private void selectMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(iteratorExp.getSource());
        currentIterator.setIterator(iteratorExp.getIterator());
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();
        currentIterator.setSource(source);

        SubSelect tempSelectSource = new SubSelect();
        tempSelectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempSelectSource = new Alias("TEMP_src");
        tempSelectSource.setAlias(aliasTempSelectSource);

        if (this.getVisitorContext().stream().map(IteratorSource::getIterator).map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            this.getVisitorContext().add(currentIterator);
        }
        SubSelect tempSelectBody = new SubSelect();
        iteratorExp.getBody().accept(this);
        tempSelectBody.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasTempSelectBody = new Alias("TEMP_body");
        tempSelectBody.setAlias(aliasTempSelectBody);

        String currentIter = iteratorExp.getIterator().getName();
        List<String> fVarsSource = VariableUtils.FVars(iteratorExp.getSource());
        List<String> fVarsBody = VariableUtils.FVars(iteratorExp.getBody());
        if (VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            if (fVarsSource.isEmpty()) {
                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);

                finalPlainSelect.setRes(new ResSelectExpression(
                        new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter))));
                finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempGBody.getName().concat(".val"))));
                finalPlainSelect.setType(new TypeSelectExpression(iteratorExp.getSource()));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }

                finalPlainSelect.setFromItem(tempGBody);
            } else {
                PlainSelect gBody = new PlainSelect();
                gBody.createTrueValColumn();
                gBody.setFromItem(tempSelectBody);
                gBody.getSelectItems().clear();
                gBody.addSelectItems(new AllColumns());

                SubSelect tempGBody = new SubSelect();
                tempGBody.setSelectBody(gBody);
                Alias aliasTempGBody = new Alias("TEMP_gbody");
                tempGBody.setAlias(aliasTempGBody);

                CaseExpression caseResExpression = new CaseExpression();
                BinaryExpression isValValid = new EqualsTo();
                isValValid.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".val")));
                isValValid.setRightExpression(new LongValue(0L));
                IsNullExpression isBodyRefNull = new IsNullExpression();
                isBodyRefNull
                        .setLeftExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));
                BinaryExpression caseBinExp = new OrExpression(isValValid, isBodyRefNull);
                WhenClause whenResClause = new WhenClause();
                whenResClause.setWhenExpression(caseBinExp);
                whenResClause.setThenExpression(new NullValue());
                caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
                caseResExpression.setElseExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));

                CaseExpression caseValExpression = new CaseExpression();
                WhenClause whenValClause = new WhenClause();
                whenValClause.setWhenExpression(caseBinExp);
                whenValClause.setThenExpression(new LongValue(0L));
                caseValExpression.setWhenClauses(Arrays.asList(whenValClause));
                caseValExpression.setElseExpression(new LongValue(1L));
                finalPlainSelect.setVal(new ValSelectExpression(caseValExpression));

                CaseExpression caseTypeExpression = new CaseExpression();
                WhenClause whenTypeClause = new WhenClause();
                whenTypeClause.setWhenExpression(caseBinExp);
                whenTypeClause.setThenExpression(new StringValue("EmptyCol"));
                caseTypeExpression.setWhenClauses(Arrays.asList(whenTypeClause));
                caseTypeExpression
                        .setElseExpression(new StringValue(iteratorExp.getSource().getElementType().getTypeName()));
                finalPlainSelect.setType(new TypeSelectExpression(caseTypeExpression));

                BinaryExpression onCondition = new EqualsTo();
                onCondition.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".res")));
                onCondition
                        .setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(currentIter)));

                List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
                for (String v : SVarsSource) {
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    BinaryExpression binExp = new EqualsTo();
                    binExp.setLeftExpression(new Column(aliasTempSelectSource.getName().concat(".ref_").concat(v)));
                    binExp.setRightExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    onCondition = new AndExpression(onCondition, binExp);
                }
                List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);
                for (String v : SVarsBody) {
                    if (v.equals(currentIter) || SVarsSource.contains(v))
                        continue;
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempGBody.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                }
                BinaryExpression bodyWhereExp = new EqualsTo();
                bodyWhereExp.setLeftExpression(new Column(aliasTempSelectBody.getName().concat(".res")));
                bodyWhereExp.setRightExpression(new LongValue(1L));
                gBody.setWhere(bodyWhereExp);

                finalPlainSelect.setFromItem(tempSelectSource);
                Join join = new Join();

                join.setRightItem(tempGBody);
                finalPlainSelect.setJoins(Arrays.asList(join));

                join.setOnExpression(onCondition);
            }

        }

        iteratorExp.setType(iteratorExp.getSource().getType());
        // create result
        finalSelect.setSelectBody(finalPlainSelect);
    }

    private void notEmptyMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.setType(new SingleType("Boolean"));
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();

        SubSelect tempNotEmptySource = new SubSelect(source.getSelectBody(), "TEMP_src");

        finalSelect.setSelectBody(finalPlainSelect);

        if (VariableUtils.FVars(iteratorExp.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempNotEmptySource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            BinaryExpression isNotEqualsZero = new NotEqualsTo();
            isNotEqualsZero.setLeftExpression(count);
            isNotEqualsZero.setRightExpression(new LongValue(0L));
            countRes.setExpression(isNotEqualsZero);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));
        } else {
            finalPlainSelect.setFromItem(tempNotEmptySource);
            Alias aliasTempNotEmptySource = tempNotEmptySource.getAlias();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempNotEmptySource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));

            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(new LongValue(1L));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);

            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for (String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempNotEmptySource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempNotEmptySource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempNotEmptySource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions(groupByExpressions);
            finalPlainSelect.setGroupByElement(groupByElement);

        }

        this.decreaseLevelOfSet();

    }

    private void emptyMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.setType(new SingleType("Boolean"));
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();

        SubSelect tempEmptySource = new SubSelect(source.getSelectBody(), "TEMP_src");

        finalSelect.setSelectBody(finalPlainSelect);

        if (VariableUtils.FVars(iteratorExp.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempEmptySource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            BinaryExpression isEqualsZero = new EqualsTo();
            isEqualsZero.setLeftExpression(count);
            isEqualsZero.setRightExpression(new LongValue(0L));
            countRes.setExpression(isEqualsZero);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));
        } else {
            finalPlainSelect.setFromItem(tempEmptySource);
            Alias aliasTempEmptySource = tempEmptySource.getAlias();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempEmptySource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(1L));

            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(new LongValue(0L));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);

            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for (String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempEmptySource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempEmptySource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempEmptySource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions(groupByExpressions);
            finalPlainSelect.setGroupByElement(groupByElement);

        }

        this.decreaseLevelOfSet();

    }

    private void collectMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        MyIteratorSource currentIterator = new MyIteratorSource();
        currentIterator.setSourceExpression(iteratorExp.getSource());
        currentIterator.setIterator(iteratorExp.getIterator());
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();
        currentIterator.setSource(source);

        SubSelect tempCollectSource = new SubSelect();
        tempCollectSource.setSelectBody(source.getSelectBody());
        Alias aliasTempCollectSource = new Alias("TEMP_src");
        tempCollectSource.setAlias(aliasTempCollectSource);

        if (this.getVisitorContext().stream().map(IteratorSource::getIterator).map(Variable::getName)
                .noneMatch(name -> currentIterator.getIterator().getName().equals(name))) {
            this.getVisitorContext().add(currentIterator);
        }
        SubSelect tempCollectBody = new SubSelect();
        iteratorExp.getBody().accept(this);
        tempCollectBody.setSelectBody(this.getFinalSelect().getSelectBody());
        Alias aliasTempCollectBody = new Alias("TEMP_body");
        tempCollectBody.setAlias(aliasTempCollectBody);

        iteratorExp.setType(iteratorExp.getBody().getType());

        String currentIter = iteratorExp.getIterator().getName();

        @SuppressWarnings("unused")
        List<String> fVarsSource = VariableUtils.FVars(iteratorExp.getSource());
        List<String> fVarsBody = VariableUtils.FVars(iteratorExp.getBody());

        if (VariableUtils.isVariableOf(fVarsBody, currentIter)) {
            finalPlainSelect.setRes(
                    new ResSelectExpression(new Column(aliasTempCollectBody.getName().concat(".").concat("res"))));
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempCollectBody.getName().concat(".val"))));
            finalPlainSelect
                    .setType(new TypeSelectExpression(new Column(aliasTempCollectBody.getName().concat(".type"))));
            finalPlainSelect.setFromItem(tempCollectBody);

            List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);
            for (String v : SVarsBody) {
                if (v.equals(currentIter))
                    continue;
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempCollectBody.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }
        } else {
            finalPlainSelect.setRes(new ResSelectExpression(new Column(aliasTempCollectBody.getName().concat(".res"))));
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasTempCollectBody.getName().concat(".val"))));
            finalPlainSelect
                    .setType(new TypeSelectExpression(new Column(aliasTempCollectBody.getName().concat(".type"))));
            finalPlainSelect.setFromItem(tempCollectSource);
            Join join = new Join();
            join.setRightItem(tempCollectBody);
            finalPlainSelect.setJoins(Arrays.asList(join));

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);
            List<String> SVarsBody = VariableUtils.SVars(iteratorExp.getBody(), this);

            List<String> sVarsIntercept = new ArrayList<String>();
            for (String sr : SVarsBody) {
                if (SVarsSource.contains(sr))
                    sVarsIntercept.add(sr);
            }
            if (!sVarsIntercept.isEmpty()) {
                join.setLeft(true);
                BinaryExpression onExp = null;
                for (String v : sVarsIntercept) {
                    BinaryExpression holderExp = new EqualsTo();
                    // TEMP_src = TEMP_src ??
                    holderExp.setLeftExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    holderExp
                            .setRightExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    //???????????????????????
                    VarSelectExpression newVar = new VarSelectExpression(v);
                    newVar.setRefExpression(new Column(aliasTempCollectSource.getName().concat(".ref_").concat(v)));
                    finalPlainSelect.addVar(newVar);
                    if (Objects.isNull(onExp)) {
                        onExp = holderExp;
                    } else {
                        onExp = new AndExpression(onExp, holderExp);
                    }
                }
                join.setOnExpression(onExp);
            }
        }
        // create result
        finalSelect.setSelectBody(finalPlainSelect);

    }

    private void sizeMap(IteratorExp iteratorExp, PlainSelect finalPlainSelect) {
        iteratorExp.setType(new SingleType("Integer"));
        iteratorExp.getSource().accept(this);
        Select source = this.getFinalSelect();

        SubSelect tempSizeSource = new SubSelect(source.getSelectBody(), "TEMP_src");

        finalSelect.setSelectBody(finalPlainSelect);

        if (VariableUtils.FVars(iteratorExp.getSource()).isEmpty()) {
            finalPlainSelect.setFromItem(tempSizeSource);
            ResSelectExpression countRes = new ResSelectExpression();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);
            countRes.setExpression(count);

            finalPlainSelect.setRes(countRes);
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));
        } else {
            finalPlainSelect.setFromItem(tempSizeSource);
            Alias aliasTempSizeSource = tempSizeSource.getAlias();

            Function count = new Function();
            count.setName("COUNT");
            count.setAllColumns(true);

            CaseExpression caseResExpression = new CaseExpression();

            BinaryExpression isValValid = new EqualsTo();
            isValValid.setLeftExpression(new Column(aliasTempSizeSource.getName().concat(".val")));
            isValValid.setRightExpression(new LongValue(0L));

            WhenClause whenResClause = new WhenClause();
            whenResClause.setWhenExpression(isValValid);
            whenResClause.setThenExpression(new LongValue(0L));

            caseResExpression.setWhenClauses(Arrays.asList(whenResClause));
            caseResExpression.setElseExpression(count);

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            finalPlainSelect.createTrueValColumn();
            finalPlainSelect.setType(new TypeSelectExpression(iteratorExp));

            List<String> SVarsSource = VariableUtils.SVars(iteratorExp.getSource(), this);

            List<Expression> groupByExpressions = new ArrayList<Expression>();

            for (String v : SVarsSource) {
                groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                VarSelectExpression newVar = new VarSelectExpression(v);
                newVar.setRefExpression(new Column(aliasTempSizeSource.getName().concat(".ref_").concat(v)));
                finalPlainSelect.addVar(newVar);
            }

            groupByExpressions.add(new Column(aliasTempSizeSource.getName().concat(".val")));

            GroupByElement groupByElement = new GroupByElement();
            groupByElement.setGroupByExpressions(groupByExpressions);
            finalPlainSelect.setGroupByElement(groupByElement);

        }

        this.decreaseLevelOfSet();

    }

    public void setContextualType(Type contextualType) {
        this.contextualType = contextualType;
        setContextualTypeToSelf();
    }

    private void setContextualTypeToSelf() {
        TypeSelectExpression type = new TypeSelectExpression(Optional.ofNullable(contextualType).map(Type::getTypeName).orElse("Unknown"));
        Select finalSelect = self.getSource();
        PlainSelect finalPlainSelect = (PlainSelect) finalSelect.getSelectBody();
        finalPlainSelect.setType(type);
        Variable holder = new Variable(self.getIterator().getName());
        self.setIterator(null);
        self.setSource(finalSelect);
        self.setIterator(holder);
    }

    public void resetVisitorContext() {
        context.clear();
        createSelfVariable();
        context.add(self);
    }
}
