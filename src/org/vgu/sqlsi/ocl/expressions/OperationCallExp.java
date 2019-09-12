/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;
import org.vgu.sqlsi.ocl.impl.OclAnySupport;
import org.vgu.sqlsi.ocl.impl.OclBooleanSupport;
import org.vgu.sqlsi.ocl.impl.OclCollectionSupport;
import org.vgu.sqlsi.ocl.impl.OclNumberSupport;
import org.vgu.sqlsi.ocl.impl.OclStringSupport;
import org.vgu.sqlsi.ocl.visitor.OCL2SQLParser;
import org.vgu.sqlsi.sql.statement.select.MyPlainSelect;
import org.vgu.sqlsi.sql.statement.select.ResSelectExpression;
import org.vgu.sqlsi.sql.statement.select.ValSelectExpression;
import org.vgu.sqlsi.sql.statement.select.VarSelectExpression;

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
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;



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
                Collection<?> bodyCollection;
                if (body == null) {
                    bodyCollection = Collections.emptyList();
                } else if (!(body instanceof Collection<?>)) {
                    bodyCollection = Collections.singletonList(body);
                } else {
                    bodyCollection = (Collection<?>) body;
                }
                return OclCollectionSupport.subtract((Collection<?>) source, bodyCollection);
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
            return OclCollectionSupport.equals((Collection<?>) source, (Collection<?>) argumentValues[0]);
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
            return OclAnySupport.oclIsKindOf(source, (Class<?>) argumentValues[0]);
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
    public Statement accept(StmVisitor visitor) {
        switch (this.name) {
        case "allInstances": {

            PlainSelect pselect = new PlainSelect();

            String type = ((TypeExp) this.getSource()).getReferredType();
            SelectExpressionItem item = new SelectExpressionItem(new Column(type.concat("_id")));
            item.setAlias(new Alias("item"));
            pselect.addSelectItems(item);
            Table table = new Table();
            table.setName(type);
            pselect.setFromItem(table);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;
        }
        case "=": {
            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());

            // 1. visit the source and save the result
            // change the alias first

            SubSelect subselect_Left = new SubSelect();
            subselect_Left.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Left = new Alias(Utilities.genAliasName(visitor));
            subselect_Left.setAlias(alias_Left);

            // 1. visit the first argument and save the result
            // change the alias first
            SubSelect subselect_Right = new SubSelect();
            subselect_Right.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Right = new Alias(Utilities.genAliasName(visitor));
            subselect_Right.setAlias(alias_Right);

            // build the expression
            PlainSelect pselect = new PlainSelect();

            BinaryExpression bexp = new EqualsTo();
            bexp.setLeftExpression(new Column(alias_Left.getName().concat(".").concat("item")));
            bexp.setRightExpression(new Column(alias_Right.getName().concat(".").concat("item")));
            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(bexp);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Left);
            ;

            Join join = new Join();
            join.setSimple(true);
            join.setRightItem(subselect_Right);

            List<Join> joins = new ArrayList<Join>();
            joins.add(join);
            pselect.setJoins(joins);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Left, subselect_Right);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;

        }
        case "<": {
            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());

            // 1. visit the source and save the result
            // change the alias first

            SubSelect subselect_Left = new SubSelect();
            subselect_Left.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Left = new Alias(Utilities.genAliasName(visitor));
            subselect_Left.setAlias(alias_Left);

            // 1. visit the first argument and save the result
            // change the alias first
            SubSelect subselect_Right = new SubSelect();
            subselect_Right.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Right = new Alias(Utilities.genAliasName(visitor));
            subselect_Right.setAlias(alias_Right);

            // build the expression
            PlainSelect pselect = new PlainSelect();

            BinaryExpression bexp = new MinorThan();
            bexp.setLeftExpression(new Column(alias_Left.getName().concat(".").concat("item")));
            bexp.setRightExpression(new Column(alias_Right.getName().concat(".").concat("item")));
            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(bexp);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Left);
            ;

            Join join = new Join();
            join.setSimple(true);
            join.setRightItem(subselect_Right);

            List<Join> joins = new ArrayList<Join>();
            joins.add(join);
            pselect.setJoins(joins);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Left, subselect_Right);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;

        }
        case "<>": {
            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());

            // 1. visit the source and save the result
            // change the alias first

            SubSelect subselect_Left = new SubSelect();
            subselect_Left.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Left = new Alias(Utilities.genAliasName(visitor));
            subselect_Left.setAlias(alias_Left);

            // 1. visit the first argument and save the result
            // change the alias first
            SubSelect subselect_Right = new SubSelect();
            subselect_Right.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Right = new Alias(Utilities.genAliasName(visitor));
            subselect_Right.setAlias(alias_Right);

            PlainSelect pselect = new PlainSelect();
            // build the expression
            BinaryExpression bexp = new NotEqualsTo();
            bexp.setLeftExpression(new Column(alias_Left.getName().concat(".").concat("item")));
            bexp.setRightExpression(new Column(alias_Right.getName().concat(".").concat("item")));
            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(bexp);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Left);
            ;

            // JOIN
            Join join = new Join();
            join.setSimple(true);
            join.setRightItem(subselect_Right);

            List<Join> joins = new ArrayList<Join>();
            joins.add(join);
            pselect.setJoins(joins);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Left, subselect_Right);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;

        }
        case "oclAsType": {

            // 1. get the "source" of the variable

            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());
            //
            // 3. visit the source and save the result
            SubSelect subselect_Source = new SubSelect();
            subselect_Source.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Source = new Alias(Utilities.genAliasName(visitor));
            subselect_Source.setAlias(alias_Source);

            // build the expression

            String type = ((VariableExp) this.getArguments().get(0)).getReferredVariable().getName();

            PlainSelect pselect = new PlainSelect();
            SelectExpressionItem item = new SelectExpressionItem(new Column(type.concat("_id")));
            item.setAlias(new Alias("item"));
            pselect.addSelectItems(item);
            Table table = new Table();
            table.setName(type);
            pselect.setFromItem(table);

            // RIGHT JOIN
            /* TESTING */
            String var_name = ((VariableExp) this.source).getReferredVariable().getName();
            // if the variable is an iterator-variable, save the
            // corresponding iteratore-source in iter
            IteratorSource iter = null;
            for (IteratorSource iter_item : visitor.getVisitorContext()) {
                if (iter_item.getIterator().getName().equals(var_name)) {
                    iter = iter_item;
                    break;
                }
            }
            if (iter != null) {
                Join join = new Join();
                join.setRight(true);
                join.setRightItem(subselect_Source);
                // set the expected on expression
                BinaryExpression bexpr = new EqualsTo();
                bexpr.setLeftExpression(new Column(alias_Source.getName().concat(".").concat("item")));
                bexpr.setRightExpression(new Column(type)); 
                join.setOnExpression(bexpr);

                List<Join> joins = new ArrayList<Join>();
                joins.add(join);
                pselect.setJoins(joins);

            } else {

                // Join join = new Join();
                // join.setLeft(true);
                // join.setRightItem(new Table(superType));
                // BinaryExpression onExp = new EqualsTo();
                // onExp.setLeftExpression(new Column(superType));
                // onExp.setRightExpression(new Column(superType + "_id"));
                // join.setOnExpression(onExp);
                // List<Join> joins = new ArrayList<Join>();
                // joins.add(join);
                // pselect.setJoins(joins);
                // where
                // BinaryExpression whereExp = new EqualsTo();
                // whereExp.setLeftExpression(new Column(superType));
                // whereExp.setRightExpression(new Column(((VariableExp)
                // this.getSource()).getReferredVariable().getName()));
                // pselect.setWhere(whereExp);

                // set the expected on expression
                // BinaryExpression bexpr = new EqualsTo();
                // bexpr.setLeftExpression(new Column(type.concat(".").concat(superType)));
                // bexpr.setRightExpression(new
                // Column(superType.concat(".").concat(superType).concat("_id")));
                // join.setOnExpression(bexpr);
            }

            // variables rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Source, null);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;

            // } else {
            // System.out.println("PROBLEM");
            // return null;
            // }
        }
        case "oclIsUndefined": {

            // 1. get the "source" of the variable

            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());
            //
            // 3. visit the source and save the result
            SubSelect subselect_Source = new SubSelect();
            subselect_Source.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Source = new Alias(Utilities.genAliasName(visitor));
            subselect_Source.setAlias(alias_Source);

            PlainSelect pselect = new PlainSelect();
            // build the expression
            IsNullExpression isnull = new IsNullExpression();
            isnull.setLeftExpression(new Column(alias_Source.getName().concat(".").concat("item")));
            isnull.setNot(false);

            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(isnull);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Source);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Source, null);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;

        }

        case "or": {
            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());

            // 1. visit the source and save the result
            // change the alias first

            SubSelect subselect_Left = new SubSelect();
            subselect_Left.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Left = new Alias(Utilities.genAliasName(visitor));
            subselect_Left.setAlias(alias_Left);

            // 1. visit the first argument and save the result
            // change the alias first
            SubSelect subselect_Right = new SubSelect();
            subselect_Right.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Right = new Alias(Utilities.genAliasName(visitor));
            subselect_Right.setAlias(alias_Right);

            PlainSelect pselect = new PlainSelect();
            // build the expression
            BinaryExpression bexp = new OrExpression(new Column(alias_Left.getName().concat(".").concat("item")),
                    new Column(alias_Right.getName().concat(".").concat("item")));
            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(bexp);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Left);
            ;

            // JOIN
            Join join = new Join();
            join.setSimple(true);
            join.setRightItem(subselect_Right);

            List<Join> joins = new ArrayList<Join>();
            joins.add(join);
            pselect.setJoins(joins);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Left, subselect_Right);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;
        }
        case "and": {
            // 1. save the current iterator source list (outer-iterators), and alias
            // in a temporal variable
            List<IteratorSource> cur_iters = new ArrayList<IteratorSource>();
            cur_iters.addAll(visitor.getVisitorContext());

            // 1. visit the source and save the result
            // change the alias first

            SubSelect subselect_Left = new SubSelect();
            subselect_Left.setSelectBody(((Select) visitor.visit(this.getSource())).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Left = new Alias(Utilities.genAliasName(visitor));
            subselect_Left.setAlias(alias_Left);

            // 1. visit the first argument and save the result
            // change the alias first
            SubSelect subselect_Right = new SubSelect();
            subselect_Right.setSelectBody(((Select) visitor.visit(this.getArguments().get(0))).getSelectBody());
            visitor.setVisitorContext(cur_iters);
            Alias alias_Right = new Alias(Utilities.genAliasName(visitor));
            subselect_Right.setAlias(alias_Right);

            PlainSelect pselect = new PlainSelect();
            // build the expression
            BinaryExpression bexp = new AndExpression(new Column(alias_Left.getName().concat(".").concat("item")),
                    new Column(alias_Right.getName().concat(".").concat("item")));
            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(bexp);
            item.setAlias(new Alias("item"));
            //
            pselect.addSelectItems(item);

            // FROM ITEM
            pselect.setFromItem(subselect_Left);
            ;

            // JOIN
            Join join = new Join();
            join.setSimple(true);
            join.setRightItem(subselect_Right);

            List<Join> joins = new ArrayList<Join>();
            joins.add(join);
            pselect.setJoins(joins);

            // rippling up
            Utilities.ripplingUpVariables(pselect, null, subselect_Left, subselect_Right);

            Select select = new Select();
            select.setSelectBody(pselect);

            return select;
        }
        default:
            return null;

        }
    }

    @Override
    public Statement map(StmVisitor visitor) {
        MyPlainSelect finalPlainSelect = new MyPlainSelect();
        
        if("allInstances".equals(this.name)) {
            ((OCL2SQLParser) visitor).increaseLevelOfSet();
            String tableName = ((TypeExp) this.getSource()).getReferredType();
            if(!org.vgu.sqlsi.main.Utilities.isClass(visitor.getPlainUMLContext(), tableName)) {
                throw new NullPointerException("Invalid class: ".concat(tableName));
            }
            ResSelectExpression resExpression = new ResSelectExpression(new Column(tableName.concat("_id")));
            Table table = new Table(tableName);
            finalPlainSelect.setRes(resExpression);
            finalPlainSelect.setFromItem(table);
        }
        else if("not".equals(this.name)) {
            Select select = (Select) visitor.visit(this.getArguments().get(0));
            MyPlainSelect selectBody = (MyPlainSelect) select.getSelectBody();
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
            
            List<String> sVarsSource = VariableUtils.SVars((MyPlainSelect) tempSource.getSelectBody(), visitor.getVisitorContext());
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
            
            List<String> fVarsLeft = VariableUtils.FVars((MyPlainSelect) tempLeft.getSelectBody());
            List<String> fVarsRight = VariableUtils.FVars((MyPlainSelect) tempRight.getSelectBody());
            List<String> sVarsLeft = VariableUtils.SVars((MyPlainSelect) tempLeft.getSelectBody(), visitor.getVisitorContext());
            List<String> sVarsRight = VariableUtils.SVars((MyPlainSelect) tempRight.getSelectBody(), visitor.getVisitorContext());
            
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

    private Expression getExpression(OclExpression oclExpression) {
        if(oclExpression instanceof BooleanLiteralExp)
            return new LongValue(((BooleanLiteralExp) oclExpression).isBooleanSymbol() ? "TRUE" : "FALSE");
        if(oclExpression instanceof StringLiteralExp)
            return new StringValue(((StringLiteralExp) oclExpression).getStringSymbol());
        else
            return new LongValue(((IntegerLiteralExp) oclExpression).getIntegerSymbol());
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
