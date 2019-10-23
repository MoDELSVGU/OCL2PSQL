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
            plainSelect = mapExists(iteratorExp);
            break;
        case first:
            break;
        case flatten:
            plainSelect = mapFlatten(iteratorExp);
            break;
        case forAll:
            plainSelect = mapForAll(iteratorExp);
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
        case "oclIsUndefined":
            plainSelect = mapOclIsUndefined(operationCallExp);
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

    private PlainSelect mapOclIsUndefined(OperationCallExp exp) {
        PlainSelect plainSelect = new PlainSelect();

        String tmpSourceAlias = "TEMP_SOURCE";

        OclExp srcExp = exp.getSource();
        srcExp.accept(this);

        Select sourceMap = this.getSelect();
        SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(),
                tmpSourceAlias);

        List<Variable> sVarsSrc = VariableUtils.SVars(srcExp);

        ValSelectExpression val = new ValSelectExpression(
                new Column(Arrays.asList(tmpSourceAlias, "val")));
        plainSelect.setVal(val);

        CaseExpression resCase = new CaseExpression();

        BinaryExpression caseSwitch = buildBinExp("=",
                new Column(Arrays.asList(tmpSourceAlias, "val")),
                new LongValue(0L));
        resCase.setSwitchExpression(caseSwitch);

        WhenClause when = new WhenClause();
        when.setWhenExpression(new LongValue(1L));
        when.setThenExpression(new NullValue());

        resCase.setWhenClauses(Arrays.asList(when));

        IsNullExpression isNull = new IsNullExpression();
        isNull.setLeftExpression(
                new Column(Arrays.asList(tmpSourceAlias, "res")));

        resCase.setElseExpression(isNull);

        ResSelectExpression res = new ResSelectExpression(resCase);
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                new StringValue("Boolean"));
        plainSelect.setType(type);

        VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);

        plainSelect.setFromItem(tmpSource);

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

        // Preparation
        String tmpBodyAlias = "TEMP_BODY";

        OclExp bodyExp = exp.getBody();

        bodyExp.accept(this);
        Select bodyMap = this.getSelect();
        SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(),
                tmpBodyAlias);

        String tmpSourceAlias = "TEMP_SOURCE";

        OclExp sourceExp = exp.getSource();

        sourceExp.accept(this);
        Select sourceMap = this.getSelect();
        SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(),
                tmpSourceAlias);

        Variable vExp = exp.getIterator();

        List<Variable> fvExp = VariableUtils.FVars(exp);
        boolean isEmptyFvExp = fvExp.isEmpty();

        List<Variable> fvBody = VariableUtils.FVars(bodyExp);
        boolean isVarInFvBody = fvBody.contains(vExp);

        String vName = exp.getIterator().getName();

        // Case 1
        if (isEmptyFvExp) {
            Table fromTable = new Table();

            Column valCol = new Column(fromTable, "val");
            ValSelectExpression val = new ValSelectExpression(valCol);
            plainSelect.setVal(val);

            Column resCol = new Column(fromTable, "ref_".concat(vName));
            ResSelectExpression res = new ResSelectExpression(resCol);
            plainSelect.setRes(res);

            Column typeCol = new Column(fromTable, "type");
            TypeSelectExpression type = new TypeSelectExpression(
                    typeCol);
            plainSelect.setType(type);

            // Case 1
            if (isVarInFvBody) {
                String srcType = sourceExp.getType().getReferredType()
                        .replaceAll("Col\\((\\w+)\\)", "$1");
                type.setExpression(new StringValue(srcType));

                fromTable.setName(tmpBodyAlias);

                plainSelect.setFromItem(tmpBody);

            } else {
                // Case 3
                resCol.setColumnName("res");

                fromTable.setName(tmpSourceAlias);

                plainSelect.setFromItem(tmpSource);

                Join join = new Join();

                join.setRightItem(tmpBody);

                plainSelect.setJoins(Arrays.asList(join));

            }

            BinaryExpression whereCond = buildBinExp("=",
                    new Column(Arrays.asList(tmpBodyAlias, "res")),
                    new LongValue(1L));
            plainSelect.setWhere(whereCond);

        } else { // Case 2

            // Val column
            CaseExpression valCase = new CaseExpression();

            BinaryExpression srcValEq0 = buildBinExp("=",
                    new Column(Arrays.asList(tmpSourceAlias, "val")),
                    new LongValue(0L));

            IsNullExpression bodyRefIsNull = new IsNullExpression();
            bodyRefIsNull.setLeftExpression(new Column(
                    Arrays.asList(tmpBodyAlias, "ref_".concat(vName))));

            BinaryExpression valAndResCaseSwitch = buildBinExp("or",
                    srcValEq0, bodyRefIsNull);

            valCase.setSwitchExpression(valAndResCaseSwitch);

            WhenClause valWhen1Then0 = new WhenClause();
            valWhen1Then0.setWhenExpression(new LongValue(1L));
            valWhen1Then0.setThenExpression(new LongValue(0L));

            valCase.setWhenClauses(Arrays.asList(valWhen1Then0));

            valCase.setElseExpression(new LongValue(1L));

            ValSelectExpression val = new ValSelectExpression(valCase);
            plainSelect.setVal(val);

            // Res column
            CaseExpression resCase = new CaseExpression();

            resCase.setSwitchExpression(valAndResCaseSwitch);

            WhenClause resWhen1ThenNull = new WhenClause();
            resWhen1ThenNull.setWhenExpression(new LongValue(1L));
            resWhen1ThenNull.setThenExpression(new NullValue());

            resCase.setWhenClauses(Arrays.asList(resWhen1ThenNull));

            resCase.setElseExpression(
                    new Column(Arrays.asList(tmpSourceAlias, "res")));

            ResSelectExpression res = new ResSelectExpression(resCase);
            plainSelect.setRes(res);

            // Type column
            TypeSelectExpression type = new TypeSelectExpression(
                    new Column(Arrays.asList(tmpSourceAlias, "type")));
            plainSelect.setType(type);

            plainSelect.setFromItem(tmpSource);

            Join join = new Join();

            // Case 2
            if (isVarInFvBody) {
                join.setLeft(true);

            } else {
                // Case 4
                // No need!!
            }

            // Right item of Left join
            PlainSelect tmpSelect = new PlainSelect();
            tmpSelect.addSelectItems(new AllColumns());

            String tmpSelectAlias = "TEMP";
            SubSelect tmp = new SubSelect(bodyMap.getSelectBody(),
                    tmpSelectAlias);

            tmpSelect.setFromItem(tmp);

            BinaryExpression tmpResEq1 = buildBinExp("=",
                    new Column(Arrays.asList(tmpSelectAlias, "res")),
                    new LongValue(1L));

            tmpSelect.setWhere(tmpResEq1);

            SubSelect tmpBodyInJoin = new SubSelect(tmpSelect,
                    tmpBodyAlias);

            join.setRightItem(tmpBodyInJoin);

            BinaryExpression onCond = buildBinExp("=",
                    new Column(Arrays.asList(tmpSourceAlias, "res")),
                    new Column(Arrays.asList(tmpBodyAlias,
                            "ref_".concat(vName))));

            List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);

            for (Variable vPrime : sVarsSrc) {
                BinaryExpression refBinExp = buildBinExp("=",
                        new Column(Arrays.asList(tmpSourceAlias,
                                "ref_".concat(vPrime.getName()))),
                        new Column(Arrays.asList(tmpBodyAlias,
                                "ref_".concat(vPrime.getName()))));

                onCond = buildBinExp("and", onCond, refBinExp);
            }

            join.setOnExpression(onCond);

            plainSelect.setJoins(Arrays.asList(join));
        }

        return plainSelect;
    }

    private PlainSelect mapForAll(IteratorExp exp) {
        // Preparation for Case 1, 2, 3, 4
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        TypeSelectExpression type = new TypeSelectExpression(
                new StringValue("Boolean"));
        plainSelect.setType(type);

        Function countAll = new Function();
        countAll.setName("COUNT");
        countAll.setAllColumns(true);

        BinaryExpression countAllEq0 = buildBinExp("=", countAll,
                new LongValue(0L));
        // .END preparation

        String tmpSourceAlias = "TEMP_SOURCE";
        OclExp sourceExp = exp.getSource();
        sourceExp.accept(this);

        Select sourceMap = this.getSelect();
        SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(),
                tmpSourceAlias);

        String tmpBodyAlias = "TEMP_BODY";
        OclExp bodyExp = exp.getBody();
        bodyExp.accept(this);

        Select bodyMap = this.getSelect();
        SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(),
                tmpBodyAlias);

        Variable v = exp.getIterator();

        List<Variable> fVarsBody = VariableUtils.FVars(bodyExp);
        boolean isVarInFvBody = fVarsBody.contains(v);

        List<Variable> fVarsExp = VariableUtils.FVars(exp);
        boolean isEmptyFvExp = fVarsExp.isEmpty();

        if (isEmptyFvExp) {
            ResSelectExpression res = new ResSelectExpression(
                    countAllEq0);
            plainSelect.setRes(res);

            // Case 1
            if (isVarInFvBody) {
                plainSelect.setFromItem(tmpBody);

            } else { // Case 3
                plainSelect.setFromItem(tmpSource);

                Join join = new Join();

                join.setRightItem(tmpBody);

                plainSelect.setJoins(Arrays.asList(join));
            }

            BinaryExpression where = buildBinExp("=",
                    new Column(Arrays.asList(tmpBodyAlias, "res")),
                    new LongValue(0L));

            plainSelect.setWhere(where);

        } else {
            // Preparation for Case 2, 4
            List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);
            List<Variable> sVarsBody = VariableUtils.SVars(bodyExp);

            List<Variable> sVarsComplement = new ArrayList<Variable>(
                    sVarsBody);
            sVarsComplement.removeAll(sVarsSrc);
            sVarsComplement.remove(v);
            // .End preparation

            CaseExpression resCase = new CaseExpression();

            IsNullExpression isNullSrcRef = new IsNullExpression();
            isNullSrcRef.setLeftExpression(new Column(Arrays.asList(
                    tmpSourceAlias, "ref_".concat(v.getName()))));

            resCase.setSwitchExpression(isNullSrcRef);

            WhenClause when1Then1 = new WhenClause();
            when1Then1.setWhenExpression(new LongValue(1L));
            when1Then1.setThenExpression(new LongValue(1L));

            resCase.setWhenClauses(Arrays.asList(when1Then1));

            resCase.setElseExpression(
                    new Column(Arrays.asList(tmpBodyAlias, "res")));

            ResSelectExpression res = new ResSelectExpression(resCase);

            plainSelect.setRes(res);

            VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);
            VariableUtils.addVar(sVarsComplement, plainSelect,
                    tmpBodyAlias);

            plainSelect.setFromItem(tmpSource);

            Join join = new Join();

            // Case 2
            if (isVarInFvBody) {
                join.setLeft(true);

            } else { // Case 4
                // No need!
            }

            // Right item of left join
            PlainSelect plainSelectInJoin = new PlainSelect();

            ResSelectExpression resInJoin = new ResSelectExpression(
                    countAllEq0);
            plainSelectInJoin.setRes(resInJoin);

            List<Variable> sVarsBodyLessV = new ArrayList<Variable>(
                    sVarsBody);
            sVarsBodyLessV.remove(v);

            VariableUtils.addVar(sVarsBodyLessV, plainSelectInJoin,
                    tmpBodyAlias);

            plainSelectInJoin.setFromItem(tmpBody);

            Function ifNull = new Function();
            ifNull.setName("IFNULL");

            ExpressionList ifNullParams = new ExpressionList(
                    new Column(Arrays.asList(tmpBodyAlias, "res")),
                    new LongValue(0L));
            ifNull.setParameters(ifNullParams);

            BinaryExpression ifNullEq0 = buildBinExp("=", ifNull,
                    new LongValue(0L));

            BinaryExpression tmpBodyValEq1 = buildBinExp("=",
                    new Column(Arrays.asList(tmpBodyAlias, "val")),
                    new LongValue(1L));

            BinaryExpression whereCond = buildBinExp("and", ifNullEq0,
                    tmpBodyValEq1);
            plainSelect.setWhere(whereCond);

            List<Expression> groupByExps = new ArrayList<Expression>();
            VariableUtils.addVarToList(sVarsBodyLessV, groupByExps,
                    tmpBodyAlias);

            GroupByElement groupBy = new GroupByElement();
            groupBy.setGroupByExpressions(groupByExps);

            plainSelectInJoin.setGroupByElement(groupBy);

            SubSelect tmpBodyInJoin = new SubSelect(plainSelectInJoin,
                    tmpBodyAlias);

            join.setRightItem(tmpBodyInJoin);

            plainSelect.setJoins(Arrays.asList(join));

            BinaryExpression onExp = null;

            for (Variable refSrc : sVarsSrc) {
                BinaryExpression refSrcEqRefBody = buildBinExp("=",
                        new Column(Arrays.asList(tmpSourceAlias,
                                "ref_".concat(refSrc.getName()))),
                        new Column(Arrays.asList(tmpBodyAlias,
                                "ref_".concat(refSrc.getName()))));

                if (onExp == null) {
                    onExp = refSrcEqRefBody;
                } else {
                    onExp = buildBinExp("and", onExp, refSrcEqRefBody);
                }
            }

            join.setOnExpression(onExp);
        }

        return plainSelect;
    }

    private PlainSelect mapExists(IteratorExp exp) {
        // Preparation for Case 1, 2, 3, 4
        PlainSelect plainSelect = new PlainSelect();
        plainSelect.createTrueValColumn();

        TypeSelectExpression type = new TypeSelectExpression(
                new StringValue("Boolean"));
        plainSelect.setType(type);

        Function countAll = new Function();
        countAll.setName("COUNT");
        countAll.setAllColumns(true);

        BinaryExpression countAllEq0 = buildBinExp(">", countAll,
                new LongValue(0L));
        // .END preparation

        String tmpSourceAlias = "TEMP_SOURCE";
        OclExp sourceExp = exp.getSource();
        sourceExp.accept(this);

        Select sourceMap = this.getSelect();
        SubSelect tmpSource = new SubSelect(sourceMap.getSelectBody(),
                tmpSourceAlias);

        String tmpBodyAlias = "TEMP_BODY";
        OclExp bodyExp = exp.getBody();
        bodyExp.accept(this);

        Select bodyMap = this.getSelect();
        SubSelect tmpBody = new SubSelect(bodyMap.getSelectBody(),
                tmpBodyAlias);

        Variable v = exp.getIterator();

        List<Variable> fVarsBody = VariableUtils.FVars(bodyExp);
        boolean isVarInFvBody = fVarsBody.contains(v);

        List<Variable> fVarsExp = VariableUtils.FVars(exp);
        boolean isEmptyFvExp = fVarsExp.isEmpty();

        if (isEmptyFvExp) {
            ResSelectExpression res = new ResSelectExpression(
                    countAllEq0);
            plainSelect.setRes(res);

            // Case 1
            if (isVarInFvBody) {
                plainSelect.setFromItem(tmpBody);

            } else { // Case 3
                plainSelect.setFromItem(tmpSource);

                Join join = new Join();

                join.setRightItem(tmpBody);

                plainSelect.setJoins(Arrays.asList(join));
            }

            BinaryExpression where = buildBinExp("=",
                    new Column(Arrays.asList(tmpBodyAlias, "res")),
                    new LongValue(1L));

            plainSelect.setWhere(where);

        } else {
            // Preparation for Case 2, 4
            List<Variable> sVarsSrc = VariableUtils.SVars(sourceExp);
            List<Variable> sVarsBody = VariableUtils.SVars(bodyExp);

            List<Variable> sVarsComplement = new ArrayList<Variable>(
                    sVarsBody);
            sVarsComplement.removeAll(sVarsSrc);
            sVarsComplement.remove(v);
            // .End preparation

            CaseExpression resCase = new CaseExpression();

            IsNullExpression isNullSrcRef = new IsNullExpression();
            isNullSrcRef.setLeftExpression(new Column(Arrays.asList(
                    tmpSourceAlias, "ref_".concat(v.getName()))));

            resCase.setSwitchExpression(isNullSrcRef);

            WhenClause when1Then1 = new WhenClause();
            when1Then1.setWhenExpression(new LongValue(1L));
            when1Then1.setThenExpression(new LongValue(1L));

            resCase.setWhenClauses(Arrays.asList(when1Then1));

            resCase.setElseExpression(
                    new Column(Arrays.asList(tmpBodyAlias, "res")));

            ResSelectExpression res = new ResSelectExpression(resCase);

            plainSelect.setRes(res);

            VariableUtils.addVar(sVarsSrc, plainSelect, tmpSourceAlias);
            VariableUtils.addVar(sVarsComplement, plainSelect,
                    tmpBodyAlias);

            plainSelect.setFromItem(tmpSource);

            Join join = new Join();

            // Case 2
            if (isVarInFvBody) {
                join.setLeft(true);

            } else { // Case 4
                // No need!
            }

            // Right item of left join
            PlainSelect plainSelectInJoin = new PlainSelect();

            ResSelectExpression resInJoin = new ResSelectExpression(
                    countAllEq0);
            plainSelectInJoin.setRes(resInJoin);

            List<Variable> sVarsBodyLessV = new ArrayList<Variable>(
                    sVarsBody);
            sVarsBodyLessV.remove(v);

            VariableUtils.addVar(sVarsBodyLessV, plainSelectInJoin,
                    tmpBodyAlias);

            plainSelectInJoin.setFromItem(tmpBody);

            BinaryExpression whereCond = buildBinExp("=",
                    new Column(Arrays.asList(tmpBodyAlias, "res")),
                    new LongValue(1L));
            plainSelect.setWhere(whereCond);

            List<Expression> groupByExps = new ArrayList<Expression>();
            VariableUtils.addVarToList(sVarsBodyLessV, groupByExps,
                    tmpBodyAlias);

            GroupByElement groupBy = new GroupByElement();
            groupBy.setGroupByExpressions(groupByExps);

            plainSelectInJoin.setGroupByElement(groupBy);

            SubSelect tmpBodyInJoin = new SubSelect(plainSelectInJoin,
                    tmpBodyAlias);

            join.setRightItem(tmpBodyInJoin);

            plainSelect.setJoins(Arrays.asList(join));

            BinaryExpression onExp = null;

            for (Variable refSrc : sVarsSrc) {
                BinaryExpression refSrcEqRefBody = buildBinExp("=",
                        new Column(Arrays.asList(tmpSourceAlias,
                                "ref_".concat(refSrc.getName()))),
                        new Column(Arrays.asList(tmpBodyAlias,
                                "ref_".concat(refSrc.getName()))));

                if (onExp == null) {
                    onExp = refSrcEqRefBody;
                } else {
                    onExp = buildBinExp("and", onExp, refSrcEqRefBody);
                }
            }

            join.setOnExpression(onExp);
        }

        return plainSelect;
    }

    private PlainSelect mapFlatten(IteratorExp exp) {
        if (!(exp.getSource() instanceof IteratorExp)) {
            throw new OclException("Cannot flatten a source which is "
                    + "not a collection.");
        }

        PlainSelect plainSelect = new PlainSelect();
        String tmpSourceAlias = "TEMP_SOURCE";

        IteratorExp tmpSourceExp = (IteratorExp) exp.getSource();
        tmpSourceExp.accept(this);
        Select tmpSourceMap = this.getSelect();

        SubSelect tmpSource = new SubSelect(
                tmpSourceMap.getSelectBody(), tmpSourceAlias);

        TypeSelectExpression type = new TypeSelectExpression(
                new Column(Arrays.asList(tmpSourceAlias, "type")));

        List<Variable> fVarsExp = VariableUtils.FVars(exp);
        boolean isEmptyFvExp = fVarsExp.isEmpty();

        if (isEmptyFvExp) {
            plainSelect.createTrueValColumn();

            ResSelectExpression res = new ResSelectExpression(
                    new Column(Arrays.asList(tmpSourceAlias, "res")));
            plainSelect.setRes(res);

            plainSelect.setType(type);

            plainSelect.setFromItem(tmpSource);

            BinaryExpression where = buildBinExp("=",
                    new Column(Arrays.asList(tmpSourceAlias, "val")),
                    new LongValue(1L));
            plainSelect.setWhere(where);

        } else {
            String tmpFlattenAlias = "TEMP_FLATTEN";
            String tmpAlias = "TEMP";

            Variable v = ((IteratorExp) exp.getSource()).getIterator();
            String vName = v.getName();

            OclExp srcOfSrcExp = ((IteratorExp) exp.getSource())
                    .getSource();
            srcOfSrcExp.accept(this);
            Select srcOfSrcMap = this.getSelect();
            SubSelect tmpSourceOfSource = new SubSelect(
                    srcOfSrcMap.getSelectBody(), tmpSourceAlias);

            List<Variable> sVarsSrcOfSrcExp = VariableUtils
                    .SVars(((IteratorExp) exp.getSource()).getSource());

            List<Variable> fVarsBody = VariableUtils
                    .FVars(exp.getBody());
            boolean isVarInBody = fVarsBody.contains(v);
            isVarInBody = true; // assumtion on paper

            if (isVarInBody) {
                CaseExpression valCase = new CaseExpression();

                IsNullExpression isNull = new IsNullExpression();
                isNull.setLeftExpression(new Column(
                        Arrays.asList(tmpFlattenAlias, "val")));

                valCase.setSwitchExpression(isNull);

                WhenClause when1Then0 = new WhenClause();
                when1Then0.setWhenExpression(new LongValue(1L));
                when1Then0.setThenExpression(new LongValue(0L));

                valCase.setWhenClauses(Arrays.asList(when1Then0));

                valCase.setElseExpression(new Column(
                        Arrays.asList(tmpFlattenAlias, "val")));

                ValSelectExpression val = new ValSelectExpression(
                        valCase);
                plainSelect.setVal(val);

                CaseExpression resCase = new CaseExpression();

                resCase.setSwitchExpression(isNull);

                WhenClause when1ThenNull = new WhenClause();
                when1ThenNull.setWhenExpression(new LongValue(1L));
                when1ThenNull.setThenExpression(new NullValue());

                resCase.setWhenClauses(Arrays.asList(when1ThenNull));

                resCase.setElseExpression(new Column(
                        Arrays.asList(tmpFlattenAlias, "res")));

                ResSelectExpression res = new ResSelectExpression(
                        resCase);
                plainSelect.setRes(res);

                plainSelect.setType(type);

                VariableUtils.addVar(sVarsSrcOfSrcExp, plainSelect,
                        tmpFlattenAlias);

                plainSelect.setFromItem(tmpSourceOfSource);

                Join join = new Join();
                join.setLeft(true);

                // Right item of left join
                PlainSelect plainSelectInJoin = new PlainSelect();
                plainSelectInJoin.addSelectItems(new AllColumns());

                SubSelect tmp = new SubSelect(
                        tmpSourceMap.getSelectBody(), tmpAlias);
                plainSelectInJoin.setFromItem(tmp);

                BinaryExpression valEq1 = buildBinExp("=",
                        new Column(Arrays.asList(tmpAlias, "val")),
                        new LongValue(1L));
                plainSelectInJoin.setWhere(valEq1);

                SubSelect tmpFlatten = new SubSelect(plainSelectInJoin,
                        tmpFlattenAlias);
                join.setRightItem(tmpFlatten);
                // .END Right item

                List<Variable> sVarsSrcOfSrcAndV = new ArrayList<Variable>(
                        sVarsSrcOfSrcExp);
                sVarsSrcOfSrcAndV.add(v);

                BinaryExpression onExp = null;
                for (Variable var : sVarsSrcOfSrcExp) {
                    BinaryExpression refSrcEqRefFlatten = buildBinExp(
                            "=",
                            new Column(Arrays.asList(tmpSourceAlias,
                                    "ref_".concat(var.getName()))),
                            new Column(Arrays.asList(tmpFlattenAlias,
                                    "ref_".concat(var.getName()))));

                    if (onExp == null) {
                        onExp = refSrcEqRefFlatten;
                    } else {
                        onExp = buildBinExp("and", onExp,
                                refSrcEqRefFlatten);
                    }
                }
                
                join.setOnExpression(onExp);
                
                plainSelect.setJoins(Arrays.asList(join));
            }
        }

        return plainSelect;
    }

    private BinaryExpression buildBinExp(String operator,
            Expression leftExp, Expression rightExp) {

        BinaryExpression binExp = null;

        switch (operator.toLowerCase()) {
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
