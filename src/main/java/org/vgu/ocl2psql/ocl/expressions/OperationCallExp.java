/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.ocl.impl.OclAnySupport;
import org.vgu.ocl2psql.ocl.impl.OclBooleanSupport;
import org.vgu.ocl2psql.ocl.impl.OclCollectionSupport;
import org.vgu.ocl2psql.ocl.impl.OclNumberSupport;
import org.vgu.ocl2psql.ocl.impl.OclStringSupport;
import org.vgu.ocl2psql.ocl.visitor.OCL2SQLParser;
import org.vgu.ocl2psql.sql.statement.select.Join;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;



/**
 * Class OperationCallExp
 */
public final class OperationCallExp extends FeatureCallExp {

    private final String name;
    
    private final List<OclExpression> arguments;

    public String getName() {
        return name;
    }

    public List<OclExpression> getArguments() {
        return arguments;
    }

    public OperationCallExp(OclExpression source, String name, OclExpression... arguments) {
        super(source);
        this.name = name;
        this.arguments = Arrays.asList(arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
        Object source = this.source.eval(context);
        if ("oclIsUndefined".equals(this.name)) {
            return OclAnySupport.oclIsUndefined(source);
        } else if (source == null && "=".equals(this.name) && this.arguments.size() == 1) {
            return this.arguments.get(0) == null || this.arguments.get(0) instanceof NullLiteralExp;
        } else if (source == null && !"oclIsKindOf".equals(this.name))
            // Bugfix: as of OCL Specification chapter 11.2.3 each
            // property
            // accessed from OclUndefined results in OclUndefined
            // this means, that a call with source == null results in
            // null!
            // throw new
            // OclEvaluationException("OclUndefined has no operation "
            // + this.name);
            return null;
        Object[] argumentValues = new Object[this.arguments.size()];
        for (int i = 0; i < argumentValues.length; i++) {
            argumentValues[i] = this.arguments.get(i).eval(context);
        }
        if ("+".equals(this.name)) {
            if (argumentValues.length != 1)
                throw new OclEvaluationException(
                        "Operation + is not defined for source and " + argumentValues.length + " arguments!");

            if (source instanceof String)
                return OclStringSupport.add((String) source, argumentValues[0]);
            else if (source instanceof Number && argumentValues[0] instanceof Number)
                return OclNumberSupport.add((Number) source, (Number) argumentValues[0]);
            else if (source instanceof Number && argumentValues[0] instanceof String)
                return OclStringSupport.add((Number) source, (String) argumentValues[0]);
            else
                throw new OclEvaluationException("Operation + is not defined for " + source.getClass().getName()
                        + " and " + argumentValues[0].getClass().getName() + " arguments!");
        } else if ("-".equals(this.name)) {
            if (argumentValues.length != 1)
                throw new OclEvaluationException(
                        "Operation - is not defined for source and " + argumentValues.length + " arguments!");
            if (source instanceof Number && argumentValues[0] instanceof Number)
                return OclNumberSupport.subtract((Number) source, (Number) argumentValues[0]);
            else if (source instanceof Collection) {
                Object body = argumentValues[0];
                Collection<Object> bodyCollection;
                if (body == null) {
                    bodyCollection = Collections.emptyList();
                } else if (!(body instanceof Collection)) {
                    bodyCollection = Collections.singletonList(body);
                } else {
                    bodyCollection = (Collection<Object>) body;
                }
                return OclCollectionSupport.subtract((Collection<Object>) source, bodyCollection);
            } else
                throw new OclEvaluationException("Operation - is not defined for " + source.getClass().getName()
                        + " and " + argumentValues[0].getClass().getName() + " arguments!");
        } else if ("*".equals(this.name)) {
            if (argumentValues.length != 1)
                throw new OclEvaluationException(
                        "Operation * is not defined for source and " + argumentValues.length + " arguments!");
            if (source instanceof Number && argumentValues[0] instanceof Number)
                return OclNumberSupport.multiply((Number) source, (Number) argumentValues[0]);
            else
                throw new OclEvaluationException("Operation * is not defined for " + source.getClass().getName()
                        + " and " + argumentValues[0].getClass().getName() + " arguments!");
        } else if ("/".equals(this.name)) {
            if (argumentValues.length != 1)
                throw new OclEvaluationException(
                        "Operation / is not defined for source and " + argumentValues.length + " arguments!");
            if (source instanceof Number && argumentValues[0] instanceof Number)
                return OclNumberSupport.divide((Number) source, (Number) argumentValues[0]);
            else
                throw new OclEvaluationException("Operation / is not defined for " + source.getClass().getName()
                        + " and " + argumentValues[0].getClass().getName() + " arguments!");
        } else if ("=".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.equals((Number) source, (Number) argumentValues[0]);
        } else if ("=".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.equals((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("=".equals(this.name) && source instanceof String && argumentValues.length == 1
                && argumentValues[0] instanceof String) {
            return OclStringSupport.equals((String) source, (String) argumentValues[0]);
        } else if ("=".equals(this.name) && source instanceof Collection && argumentValues.length == 1
                && argumentValues[0] instanceof Collection) {
            return OclCollectionSupport.equals((Collection<Object>) source, (Collection<Object>) argumentValues[0]);
        } else if ("=".equals(this.name) && argumentValues.length == 1) {
            return OclAnySupport.equals(source, argumentValues[0]);
        } else if ("<>".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.notEquals((Number) source, (Number) argumentValues[0]);
        } else if ("<>".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.notEquals((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("<>".equals(this.name) && source instanceof String && argumentValues.length == 1
                && argumentValues[0] instanceof String) {
            return OclStringSupport.notEquals((String) source, (String) argumentValues[0]);
        } else if ("<>".equals(this.name) && argumentValues.length == 1) {
            return OclAnySupport.equals(source, argumentValues[0]);
        } else if (">".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.greaterThan((Number) source, (Number) argumentValues[0]);
        } else if ("<".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.lessThan((Number) source, (Number) argumentValues[0]);
        } else if (">=".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.atLeast((Number) source, (Number) argumentValues[0]);
        } else if ("<=".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.atMost((Number) source, (Number) argumentValues[0]);
        } else if ("mod".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.mod((Number) source, (Number) argumentValues[0]);
        } else if ("div".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.div((Number) source, (Number) argumentValues[0]);
        } else if ("min".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.min((Number) source, (Number) argumentValues[0]);
        } else if ("max".equals(this.name) && source instanceof Number && argumentValues.length == 1
                && argumentValues[0] instanceof Number) {
            return OclNumberSupport.max((Number) source, (Number) argumentValues[0]);
        } else if ("round".equals(this.name) && source instanceof Number && argumentValues.length == 0) {
            return OclNumberSupport.round((Number) source);
        } else if ("floor".equals(this.name) && source instanceof Number && argumentValues.length == 0) {
            return OclNumberSupport.floor((Number) source);
        } else if ("abs".equals(this.name) && source instanceof Number && argumentValues.length == 0) {
            return OclNumberSupport.abs((Number) source);
        } else if ("size".equals(this.name) && source instanceof String && argumentValues.length == 0) {
            return OclStringSupport.size((String) source);
        } else if ("toLower".equals(this.name) && source instanceof String && argumentValues.length == 0) {
            return OclStringSupport.toLower((String) source);
        } else if ("toUpper".equals(this.name) && source instanceof String && argumentValues.length == 0) {
            return OclStringSupport.toUpper((String) source);
        } else if ("concat".equals(this.name) && source instanceof String && argumentValues.length == 1
                && argumentValues[0] instanceof String) {
            return OclStringSupport.concat((String) source, (String) argumentValues[0]);
        } else if ("substring".equals(this.name) && source instanceof String && argumentValues.length == 2
                && argumentValues[0] instanceof Number && argumentValues[1] instanceof Number) {
            return OclStringSupport.substring((String) source, ((Number) argumentValues[0]).intValue(),
                    ((Number) argumentValues[1]).intValue());
        } else if ("and".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.and((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("or".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.or((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("xor".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.xor((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("implies".equals(this.name) && source instanceof Boolean && argumentValues.length == 1
                && argumentValues[0] instanceof Boolean) {
            return OclBooleanSupport.implies((Boolean) source, (Boolean) argumentValues[0]);
        } else if ("oclIsKindOf".equals(this.name) && argumentValues.length == 1
                && argumentValues[0] instanceof Class) {
            return OclAnySupport.oclIsKindOf(source, (Class<Object>) argumentValues[0]);
        } else {
            try {
                return context.callMethod(source, this.name, argumentValues);
            } catch (Exception e) {
                throw new OclEvaluationException(
                        "cannot call method: " + this.name + " on source: " + source.getClass().getName(), e);
            }
        }
    }

    @Override
    public Statement map(StmVisitor visitor) {
        PlainSelect finalPlainSelect = new PlainSelect();
        
        if("allInstances".equals(this.name)) {
            ((OCL2SQLParser) visitor).increaseLevelOfSet();
            String tableName = ((TypeExp) this.getSource()).getReferredType();
            if(!Utilities.isClass(visitor.getPlainUMLContext(), tableName)) {
                throw new NullPointerException("Invalid class: ".concat(tableName));
            }
            ResSelectExpression resExpression = new ResSelectExpression(new Column(tableName.concat("_id")));
            Table table = new Table(tableName);
            finalPlainSelect.setRes(resExpression);
            finalPlainSelect.setFromItem(table);
        }
        else if("not".equals(this.name)) {
            Select select = (Select) visitor.visit(this.getArguments().get(0));
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            ResSelectExpression curRes = selectBody.getRes();
            curRes.setExpression(new NotExpression(curRes.getExpression()));
            return select;
        }
        else if("oclIsUndefined".equals(this.name)) {
            Select select = (Select) visitor.visit(this.source);
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
            isNullExpr.setLeftExpression( new Column(aliasSource.getName().concat(".res")) );
            caseResExpression.setElseExpression(isNullExpr);
//            caseResExpression.setElseExpression(new IsNullExpression(new Column(aliasSource.getName().concat(".res"))));

            finalPlainSelect.setRes(new ResSelectExpression(caseResExpression));
            
            finalPlainSelect.setVal(new ValSelectExpression(new Column(aliasSource.getName().concat(".val"))));
            
            List<String> sVarsSource = VariableUtils.SVars(this.getSource(), visitor);
            for(String s : sVarsSource) {
                VarSelectExpression varExp = new VarSelectExpression(s);
                varExp.setRefExpression(new Column(aliasSource.getName().concat(".ref_").concat(s)));
                finalPlainSelect.addVar(varExp);
            }
        }
        else {
            SubSelect tempLeft = new SubSelect();
            tempLeft.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            Alias alias_Left = new Alias("TEMP_LEFT");
            tempLeft.setAlias(alias_Left);
            
            SubSelect tempRight = new SubSelect();
            tempRight.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            Alias alias_Right = new Alias("TEMP_RIGHT");
            tempRight.setAlias(alias_Right);
            
            List<String> fVarsLeft = VariableUtils.FVars(this.getSource());
            List<String> fVarsRight = VariableUtils.FVars(this.getArguments().get( 0 ));
            List<String> sVarsLeft = VariableUtils.SVars(this.getSource(), visitor);
            List<String> sVarsRight = VariableUtils.SVars(this.getArguments().get(0), visitor);
            
            if(fVarsLeft.isEmpty() && fVarsRight.isEmpty()) {
                ResSelectExpression resExp = new ResSelectExpression();
                BinaryExpression eqExp = generateBinaryExpression(this.name,
                    new Column(alias_Left.getName().concat(".res")),
                    new Column(alias_Right.getName().concat(".res")));
//                eqExp.setLeftExpression(new Column(alias_Left.getName().concat(".res")));
//                eqExp.setRightExpression(new Column(alias_Right.getName().concat(".res")));
                resExp.setExpression(eqExp);
                finalPlainSelect.setRes(resExp);
                finalPlainSelect.setFromItem(tempLeft);
                finalPlainSelect.setValAsTrue();
                Join join = new Join();
                join.setSimple(true);
                join.setRightItem(tempRight);
                finalPlainSelect.setJoins(Arrays.asList(join));
            }
            else if(!fVarsLeft.isEmpty() && sVarsRight.stream().allMatch(vr -> sVarsLeft.contains(vr))) {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);

                BinaryExpression eqExp = generateBinaryExpression(this.name,
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
                
                for(String s : sVarsLeft) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Left.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }
                
                finalPlainSelect.setFromItem(tempLeft);
                
                Join join = new Join();
                join.setRightItem(tempRight);
                finalPlainSelect.setJoins(Arrays.asList(join));
                List<String> sVarsIntercept = new ArrayList<String>();
                for(String sr : sVarsRight) {
                    if(sVarsLeft.contains(sr)) sVarsIntercept.add(sr);
                }
                if(!sVarsIntercept.isEmpty()) {
                    join.setRight(true);
                    BinaryExpression onExp = null;
                    for(String v : sVarsIntercept) {
                        if(Objects.isNull(onExp)) {
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
            }
            else if(!fVarsRight.isEmpty() && sVarsLeft.stream().allMatch(vl -> sVarsRight.contains(vl))) {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);

                BinaryExpression eqExp = generateBinaryExpression(this.name,
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
                
                for(String s : sVarsRight) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Right.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }
                
                finalPlainSelect.setFromItem(tempRight);
                
                Join join = new Join();
                join.setRightItem(tempLeft);
                finalPlainSelect.setJoins(Arrays.asList(join));
                List<String> sVarsIntercept = new ArrayList<String>();
                for(String sl : sVarsLeft) {
                    if(sVarsRight.contains(sl)) sVarsIntercept.add(sl);
                }
                if(!sVarsIntercept.isEmpty()) {
                    join.setLeft(true);
                    BinaryExpression onExp = null;
                    for(String v : sVarsIntercept) {
                        if(Objects.isNull(onExp)) {
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
            }
            else {
                BinaryExpression leftValEq = new EqualsTo();
                leftValEq.setLeftExpression(new Column(alias_Left.getName().concat(".val")));
                leftValEq.setRightExpression(new LongValue(0L));
                BinaryExpression rightValEq = new EqualsTo();
                rightValEq.setLeftExpression(new Column(alias_Right.getName().concat(".val")));
                rightValEq.setRightExpression(new LongValue(0L));
                OrExpression orExp = new OrExpression(leftValEq, rightValEq);
                BinaryExpression eqExp = generateBinaryExpression(this.name,
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
                
                for(String s : sVarsLeft) {
                    VarSelectExpression varExp = new VarSelectExpression(s);
                    varExp.setRefExpression(new Column(alias_Left.getName().concat(".ref_").concat(s)));
                    finalPlainSelect.addVar(varExp);
                }
                for(String s : sVarsRight) {
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
        
        Select finalSelect = new Select();
        finalSelect.setSelectBody(finalPlainSelect);

        return finalSelect;
    }

    private BinaryExpression generateBinaryExpression(
        String operation, 
        Expression leftExpression,
        Expression rightExpression) {

        BinaryExpression binaryExpr = null;

        switch ( operation ) {
            case "=" :
                binaryExpr = new EqualsTo();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case "<>" :
                binaryExpr = new NotEqualsTo();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case ">" :
                binaryExpr = new GreaterThan();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case "<" :
                binaryExpr = new MinorThan();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case ">=" :
                binaryExpr = new GreaterThanEquals();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case "<=" :
                binaryExpr = new MinorThanEquals();
                binaryExpr.setLeftExpression( leftExpression );
                binaryExpr.setRightExpression( rightExpression );
                return binaryExpr;
            case "and" :
                return new AndExpression( leftExpression, rightExpression );
            case "or" :
                return new OrExpression( leftExpression, rightExpression );
            default :
                return null;
        }
    }

}
