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

@author: thian
***************************************************************************/

package org.vgu.ocl2psql.ocl.parser.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.ocl.parser.exception.OclException;
import org.vgu.ocl2psql.ocl.parser.utils.VariableUtils;
import org.vgu.ocl2psql.sql.statement.select.Join;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.SubSelect;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.IntegerLiteralExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.IteratorKind;
import com.vgu.se.jocl.expressions.LiteralExp;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.RealLiteralExp;
import com.vgu.se.jocl.expressions.StringLiteralExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.expressions.VariableExp;
import com.vgu.se.jocl.visit.ParserVisitor;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
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
import net.sf.jsqlparser.statement.select.GroupByElement;

public class SimpleOclParser implements ParserVisitor {

    private Select select;

    public Select getSelect() {
        return this.select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    private void addComment(OclExp exp, PlainSelect plainSelect) {
        plainSelect.setCorrespondOCLExpression(exp.getOclStr());
    }

    @Override
    public void visit(OclExp oclExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IteratorExp iteratorExp) {
        PlainSelect plainSelect = null;

        switch (IteratorKind.valueOf(iteratorExp.getKind())) {
        case any:
            break;
        case asBag:
            break;
        case asOrderedSet:
            break;
        case asSequence:
            break;
        case asSet:
            break;
        case at:
            break;
        case collect:
            plainSelect = mapCollect(iteratorExp);
            break;
        case count:
            break;
        case excludes:
            break;
        case excludesAll:
            break;
        case excluding:
            break;
        case exists:
            break;
        case first:
            break;
        case flatten:
            break;
        case forAll:
            break;
        case includes:
            break;
        case includesAll:
            break;
        case including:
            break;
        case indexOf:
            break;
        case isEmpty:
            break;
        case isUnique:
            break;
        case last:
            break;
        case notEmpty:
            break;
        case one:
            break;
        case reject:
            break;
        case select:
            plainSelect = mapSelect(iteratorExp);
            break;
        case size:
            plainSelect = mapSize(iteratorExp);
            break;
        case sortedBy:
            break;
        case sum:
            break;
        case union:
            break;
        default:
            break;
        }

        addComment(iteratorExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(OperationCallExp operationCallExp) {
        PlainSelect plainSelect = null;

        switch (operationCallExp.getReferredOperation().getName()) {
        case "allInstances":
            plainSelect = mapAllInstances(operationCallExp);
            break;
        case "=":
        case "<>":
        case "<=":
        case ">=":
        case ">":
        case "<":
        case "and":
        case "or":
            plainSelect = mapBinary(operationCallExp);
            break;
        default:
            break;
        }
        
        addComment(operationCallExp, plainSelect);
        
        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(LiteralExp literalExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(PropertyCallExp propertyCallExp) {
        PlainSelect plainSelect = map(propertyCallExp);

        addComment(propertyCallExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(AssociationClassCallExp associationClassCallExp) {
        PlainSelect plainSelect = map(associationClassCallExp);

        addComment(associationClassCallExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(StringLiteralExp stringLiteralExp) {
        PlainSelect plainSelect = map(stringLiteralExp);

        addComment(stringLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(BooleanLiteralExp booleanLiteralExp) {
        PlainSelect plainSelect = map(booleanLiteralExp);

        addComment(booleanLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(IntegerLiteralExp integerLiteralExp) {
        PlainSelect plainSelect = map(integerLiteralExp);

        addComment(integerLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
        PlainSelect plainSelect = map(realLiteralExp);

        addComment(realLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(VariableExp variableExp) {
        PlainSelect plainSelect = map(variableExp);

        addComment(variableExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }
    // ***********************************************
    // * Helpers
    // ***********************************************

    private PlainSelect map(BooleanLiteralExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new Column(exp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        return plainSelect;
    }

    private PlainSelect map(IntegerLiteralExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new LongValue(exp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        return plainSelect;
    }

    private PlainSelect map(RealLiteralExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new LongValue(exp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        return plainSelect;
    }

    private PlainSelect map(StringLiteralExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new StringValue(exp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        return plainSelect;
    }

    private PlainSelect mapAllInstances(OperationCallExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        String tableName = exp.getSource().getType().getReferredType();

        ResSelectExpression res = new ResSelectExpression(
                new Column(tableName.concat("_id")));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(tableName);
        plainSelect.setType(type);

        Table table = new Table(tableName);
        plainSelect.setFromItem(table);

        return plainSelect;
    }

    private PlainSelect mapBinary(OperationCallExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        OclExp leftExp = exp.getSource();
        OclExp rightExp = exp.getArguments().get(0);

        String tmpLeftAlias = "TEMP_LEFT";
        String tmpRightAlias = "TEMP_RIGHT";

        List<Variable> fVarsLeft = VariableUtils.FVars(leftExp);
        List<Variable> fVarsRight = VariableUtils.FVars(rightExp);

        boolean isEmptyFvLeft = fVarsLeft.isEmpty();
        boolean isEmptyFvRight = fVarsRight.isEmpty();

        BinaryExpression binExp = buildBinExp(
                exp.getReferredOperation().getName(),
                new Column(Arrays.asList(tmpLeftAlias, "res")),
                new Column(Arrays.asList(tmpRightAlias, "res")));

        ResSelectExpression res = new ResSelectExpression(binExp);
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                new StringValue("boolean"));
        plainSelect.setType(type);

        leftExp.accept(this);
        Select leftMap = this.getSelect();
        SubSelect tmpLeft = new SubSelect(leftMap.getSelectBody(),
                tmpLeftAlias);

        rightExp.accept(this);
        Select rightMap = this.getSelect();
        SubSelect tmpRight = new SubSelect(rightMap.getSelectBody(),
                tmpRightAlias);

        // Case 1
        if (isEmptyFvLeft && isEmptyFvRight) {
            plainSelect.createTrueValColumn();

            plainSelect.setFromItem(tmpLeft);

            Join join = new Join();
            join.setRightItem(tmpRight);

            plainSelect.setJoins(Arrays.asList(join));

            return plainSelect;
        }

        // Preparation for Case 2, 3, 4
        List<Variable> sVarsLeft = VariableUtils.SVars(leftExp);
        List<Variable> sVarsRight = VariableUtils.SVars(rightExp);

        boolean isSubsetSvLeftOfSvRight = sVarsRight
                .containsAll(sVarsLeft);
        boolean isSubsetSvRightOfSvLeft = sVarsLeft
                .containsAll(sVarsRight);

        List<Variable> sVarsIntxn = new ArrayList<Variable>(sVarsLeft);
        sVarsIntxn.retainAll(sVarsRight);

        boolean isEmptySvIntxn = sVarsIntxn.isEmpty();

        CaseExpression caseVal = new CaseExpression();

        BinaryExpression leftValEq0 = buildBinExp("=",
                new Column(Arrays.asList(tmpLeftAlias, "val")),
                new LongValue(0L));

        BinaryExpression rightValEq0 = buildBinExp("=",
                new Column(Arrays.asList(tmpRightAlias, "val")),
                new LongValue(0L));

        OrExpression orExp = new OrExpression(leftValEq0, rightValEq0);
        caseVal.setSwitchExpression(orExp);

        WhenClause whenClause = new WhenClause();
        whenClause.setWhenExpression(new LongValue(1L));
        whenClause.setThenExpression(new LongValue(0L));
        caseVal.setWhenClauses(Arrays.asList(whenClause));

        caseVal.setElseExpression(new LongValue(1L));

        ValSelectExpression val = new ValSelectExpression(caseVal);
        plainSelect.setVal(val);

        Join join = new Join();

        if (!isEmptySvIntxn) {
            join.setLeft(true);

            for (Variable v : sVarsIntxn) {
                VarSelectExpression var = new VarSelectExpression(
                        v.getName());
                String refName = var.getRef().getAlias().getName();

                BinaryExpression onExp = buildBinExp("=",
                        new Column(
                                Arrays.asList(tmpLeftAlias, refName)),
                        new Column(
                                Arrays.asList(tmpRightAlias, refName)));

                join.setOnExpression(onExp);
            }
        }

        // Case 2
        if (!isEmptyFvLeft && isSubsetSvRightOfSvLeft) {
            VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);

            plainSelect.setFromItem(tmpLeft);
            join.setRightItem(tmpRight);
        }
        // case 3
        else if (!isEmptyFvRight && isSubsetSvLeftOfSvRight) {
            VariableUtils.addVar(sVarsRight, plainSelect,
                    tmpRightAlias);

            plainSelect.setFromItem(tmpRight);
            join.setRightItem(tmpLeft);
        }
        // case 4
        else if (!isEmptyFvLeft && !isEmptyFvLeft
                && !isSubsetSvLeftOfSvRight
                && !isSubsetSvRightOfSvLeft) {

            VariableUtils.addVar(sVarsLeft, plainSelect, tmpLeftAlias);
            VariableUtils.addVar(sVarsRight, plainSelect,
                    tmpRightAlias);

            plainSelect.setFromItem(tmpLeft);
            join.setRightItem(tmpRight);
        }

        plainSelect.setJoins(Arrays.asList(join));

        return plainSelect;
    }

    private PlainSelect mapSize(IteratorExp exp) {
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        exp.getSource().accept(this);
        Select source = this.getSelect();

        String tmpSourceAlias = "TEMP_src";
        SubSelect tmpSource = new SubSelect(source.getSelectBody(),
                tmpSourceAlias);
        plainSelect.setFromItem(tmpSource);

        TypeSelectExpression type = new TypeSelectExpression("Integer");
        plainSelect.setType(type);

        Function count = new Function();
        count.setName("COUNT");
        count.setAllColumns(true);

        if (VariableUtils.FVars(exp).isEmpty()) {
            ResSelectExpression res = new ResSelectExpression(count);
            plainSelect.setRes(res);

            return plainSelect;

        } else {
            BinaryExpression isValZero = buildBinExp("=",
                    new Column(Arrays.asList(tmpSourceAlias, "val")),
                    new LongValue(0L));

            WhenClause whenCase = new WhenClause();
            whenCase.setWhenExpression(new LongValue(1L));
            whenCase.setThenExpression(new LongValue(0L));

            CaseExpression caseRes = new CaseExpression();
            caseRes.setSwitchExpression(isValZero);
            caseRes.setWhenClauses(Arrays.asList(whenCase));
            caseRes.setElseExpression(count);

            ResSelectExpression res = new ResSelectExpression(caseRes);
            plainSelect.setRes(res);

            List<Expression> groupByFields = new ArrayList<Expression>();

            List<Variable> sVars = VariableUtils.SVars(exp);
            for (Variable v : sVars) {
                VarSelectExpression refVar = new VarSelectExpression(
                        v.getName());
                Expression varCol = new Column(
                        Arrays.asList(tmpSourceAlias,
                                refVar.getRef().getAlias().getName()));
                refVar.setRefExpression(varCol);

                groupByFields.add(varCol);
            }

            groupByFields.add(
                    new Column(Arrays.asList(tmpSourceAlias, "val")));

            GroupByElement groupByEl = new GroupByElement();
            groupByEl.setGroupByExpressions(groupByFields);
            plainSelect.setGroupByElement(groupByEl);

            return plainSelect;
        }
    }

    private PlainSelect mapCollect(IteratorExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpBodyAlias = "TEMP_body";

        ValSelectExpression val = new ValSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias, "val")));
        plainSelect.setVal(val);

        ResSelectExpression res = new ResSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias, "res")));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias, "type")));
        plainSelect.setType(type);

        exp.getBody().accept(this);
        Select body = this.getSelect();
        SubSelect tmpBody = new SubSelect(body.getSelectBody(),
                tmpBodyAlias);

        List<Variable> fVarsB = VariableUtils.FVars(exp.getBody());
        Variable v = exp.getIterator();
        boolean isVarInFVarsB = fVarsB.contains(v);

        if (isVarInFVarsB) {
            plainSelect.setFromItem(tmpBody);

        } else {
            String tmpSourceAlias = "TEMP_source";

            exp.getSource().accept(this);
            Select source = this.getSelect();
            SubSelect tmpSource = new SubSelect(source.getSelectBody(),
                    tmpSourceAlias);

            plainSelect.setFromItem(tmpSource);

            Join join = new Join();
            join.setRightItem(tmpBody);

            plainSelect.setJoins(Arrays.asList(join));
        }

        List<Variable> fVarsExp = VariableUtils.FVars(exp);
        boolean isEmptyFVarsExp = fVarsExp.isEmpty();

        if (isEmptyFVarsExp) {
            // Nothing to do

        } else {
            List<Variable> sVarsS = VariableUtils
                    .SVars(exp.getSource());
            List<Variable> sVarsB = VariableUtils.SVars(exp.getBody());
            List<Variable> sVarsRemained = VariableUtils
                    .getComplement(sVarsB, sVarsS);

            if (sVarsRemained.contains(v)) {
                sVarsRemained.remove(v);
            }

            if (isVarInFVarsB) {
                VariableUtils.addVar(sVarsS, plainSelect, tmpBodyAlias);

                VariableUtils.addVar(sVarsRemained, plainSelect,
                        tmpBodyAlias);

                plainSelect.setFromItem(tmpBody);

            } else {
                // Already handled above
            }
        }

        return plainSelect;
    }

    private PlainSelect mapSelect(IteratorExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpBodyAlias = "TEMP_body";

        String iter = exp.getIterator().getName();

        ValSelectExpression val = new ValSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias, "val")));
        plainSelect.setVal(val);

        ResSelectExpression res = new ResSelectExpression(new Column(
                Arrays.asList(tmpBodyAlias, "ref_".concat(iter))));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias, "type")));
        plainSelect.setType(type);

        return plainSelect;
    }

    private PlainSelect map(VariableExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpDmnAlias = "TEMP_dmn";
        String varName = exp.getVariable().getName();

        ValSelectExpression val = new ValSelectExpression(
                new Column(Arrays.asList(tmpDmnAlias, "val")));
        plainSelect.setVal(val);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        ResSelectExpression res = new ResSelectExpression(
                new Column(Arrays.asList(tmpDmnAlias, "res")));
        plainSelect.setRes(res);

        VarSelectExpression v = new VarSelectExpression(varName);
        v.setRefExpression(
                new Column(Arrays.asList(tmpDmnAlias, "res")));
        plainSelect.addVar(v);

        OclExp sourceExp = exp.getVariable().getSource();
        sourceExp.accept(this);
        Select source = this.getSelect();
        SubSelect tmpSource = new SubSelect(source.getSelectBody(),
                tmpDmnAlias);
        plainSelect.setFromItem(tmpSource);

        List<Variable> sVarsSrcV = VariableUtils.SVars(sourceExp);

        VariableUtils.addVar(sVarsSrcV, plainSelect, tmpDmnAlias);

        return plainSelect;
    }

    private PlainSelect map(PropertyCallExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpObjAlias = "TEMP_obj";

        String className = exp.getNavigationSource().getType()
                .getReferredType().replaceAll("Col\\((\\w+)\\)", "$1");

        ValSelectExpression val = new ValSelectExpression(
                new Column(Arrays.asList(tmpObjAlias, "val")));
        plainSelect.setVal(val);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());
        plainSelect.setType(type);

        ResSelectExpression res = new ResSelectExpression(new Column(
                Arrays.asList(className, exp.getReferredProperty())));
        plainSelect.setRes(res);

        exp.getNavigationSource().accept(this);
        Select vMap = this.getSelect();
        SubSelect tmpObj = new SubSelect(vMap.getSelectBody(),
                tmpObjAlias);
        plainSelect.setFromItem(tmpObj);

        List<Variable> sVarsV = VariableUtils
                .SVars(exp.getNavigationSource());

        VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

        Join leftJ = new Join();
        leftJ.setLeft(true);
        leftJ.setRightItem(new Table(className));
        plainSelect.setJoins(Arrays.asList(leftJ));

        BinaryExpression refVEqId = new EqualsTo();
        if (exp.getNavigationSource() instanceof VariableExp) {
            VariableExp varExp = (VariableExp) exp
                    .getNavigationSource();
            refVEqId.setLeftExpression(new Column(Arrays.asList(
                    tmpObjAlias,
                    "ref_".concat(varExp.getVariable().getName()))));
            refVEqId.setRightExpression(new Column(
                    Arrays.asList(className, className.concat("_id"))));
        } else {
            throw new OclException(
                    "Cannot parse non Variable expression");
        }

        BinaryExpression valEq1 = buildBinExp("=",
                new Column(Arrays.asList(tmpObjAlias, "val")),
                new LongValue(1L));

        BinaryExpression onExp = new AndExpression(refVEqId, valEq1);
        leftJ.setOnExpression(onExp);

        return plainSelect;
    }

    private PlainSelect map(AssociationClassCallExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpObjAlias = "TEMP_obj";

        String assoc = exp.getAssociation();
        String oppos = exp.getOppositeAssociationEnd();
        String ase = exp.getAssociationEnd();

        CaseExpression caseVal = new CaseExpression();

        IsNullExpression isOpposNull = new IsNullExpression();
        isOpposNull.setLeftExpression(
                new Column(Arrays.asList(assoc, oppos)));
        caseVal.setSwitchExpression(isOpposNull);

        WhenClause whenClause = new WhenClause();
        whenClause.setWhenExpression(new LongValue(1L));
        whenClause.setThenExpression(new LongValue(0L));
        caseVal.setWhenClauses(Arrays.asList(whenClause));

        caseVal.setElseExpression(new LongValue(1L));

        ValSelectExpression val = new ValSelectExpression(caseVal);
        plainSelect.setVal(val);

        ResSelectExpression res = new ResSelectExpression(
                new Column(Arrays.asList(assoc, ase)));
        plainSelect.setRes(res);

        CaseExpression caseType = new CaseExpression();
        caseType.setSwitchExpression(isOpposNull);

        WhenClause typeWhenClause = new WhenClause();
        typeWhenClause.setWhenExpression(new LongValue(1L));
        typeWhenClause.setThenExpression(new StringValue("EmptyCol"));
        caseType.setWhenClauses(Arrays.asList(typeWhenClause));

        String resType = exp.getOppositeAssociationEndType()
                .getReferredType().replaceAll("Col\\((\\w+)\\)", "$1");
        caseType.setElseExpression(new StringValue(resType));

        TypeSelectExpression type = new TypeSelectExpression(caseType);
        plainSelect.setType(type);

        OclExp source = exp.getNavigationSource();

        List<Variable> sVarsV = VariableUtils.SVars(source);

        VariableUtils.addVar(sVarsV, plainSelect, tmpObjAlias);

        source.accept(this);
        Select vMap = this.getSelect();
        SubSelect tmpObj = new SubSelect(vMap.getSelectBody(),
                tmpObjAlias);

        plainSelect.setFromItem(tmpObj);

        Join leftJ = new Join();
        leftJ.setLeft(true);
        leftJ.setRightItem(new Table(assoc));

        BinaryExpression refVEqOppos = new EqualsTo();
        if (exp.getNavigationSource() instanceof VariableExp) {
            VariableExp varExp = (VariableExp) exp
                    .getNavigationSource();
            refVEqOppos.setLeftExpression(new Column(Arrays.asList(
                    tmpObjAlias,
                    "ref_".concat(varExp.getVariable().getName()))));
            refVEqOppos.setRightExpression(
                    new Column(Arrays.asList(assoc, oppos)));
        } else {
            throw new OclException(
                    "Cannot parse non Variable expression");
        }

        BinaryExpression valEq1 = buildBinExp("=",
                new Column(Arrays.asList(tmpObjAlias, "val")),
                new LongValue(1L));

        BinaryExpression onExp = new AndExpression(refVEqOppos, valEq1);
        leftJ.setOnExpression(onExp);

        plainSelect.setJoins(Arrays.asList(leftJ));

        return plainSelect;
    }

    private BinaryExpression buildBinExp(String operator,
            Expression leftExp, Expression rightExp) {

        BinaryExpression binExp = null;

        switch (operator) {
        case "=":
            binExp = new EqualsTo();
            break;
        case "<>":
            binExp = new NotEqualsTo();
            break;
        case "<":
            binExp = new MinorThan();
            break;
        case "<=":
            binExp = new MinorThanEquals();
            break;
        case ">":
            binExp = new GreaterThan();
            break;
        case ">=":
            binExp = new GreaterThanEquals();
            break;
        case "and":
            binExp = new AndExpression(null, null);
            break;
        case "or":
            binExp = new OrExpression(null, null);
            break;
        }

        binExp.setLeftExpression(leftExp);
        binExp.setRightExpression(rightExp);

        return binExp;
    }
}
