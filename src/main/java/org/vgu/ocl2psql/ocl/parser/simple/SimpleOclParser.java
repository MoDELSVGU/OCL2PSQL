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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.ocl.parser.exception.OclException;
import org.vgu.ocl2psql.ocl.parser.utils.VariableUtils;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.RefSelectExpression;
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

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.GroupByElement;
import net.sf.jsqlparser.statement.select.Join;

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
        PlainSelect plainSelect = map(operationCallExp);

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

    private PlainSelect map(OperationCallExp exp) {

        switch (exp.getReferredOperation().getName()) {
        case "allInstances":
            return mapAllInstances(exp);
        default:
            return null;
        }
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
            BinaryExpression isValZero = new EqualsTo();
            isValZero.setLeftExpression(
                    new Column(tmpSourceAlias.concat(".val")));
            isValZero.setRightExpression(new LongValue(0L));

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

            groupByFields
                    .add(new Column(tmpSourceAlias.concat(".val")));

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
                new Column(Arrays.asList(tmpBodyAlias,"val")));
        plainSelect.setVal(val);

        ResSelectExpression res = new ResSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias,"res")));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                new Column(Arrays.asList(tmpBodyAlias,"type")));
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
            List<Variable> sVarsS = VariableUtils.SVars(exp.getSource());
            List<Variable> sVarsB = VariableUtils.SVars(exp.getBody());
            List<Variable> sVarsRemained = VariableUtils
                    .getComplement(sVarsB, sVarsS);

            if (sVarsRemained.contains(v)) {
                sVarsRemained.remove(v);
            }

            if (isVarInFVarsB) {
                VariableUtils.addVar(sVarsS, plainSelect,
                        tmpBodyAlias);

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
                new Column(tmpDmnAlias.concat(".val")));
        plainSelect.setVal(val);

        TypeSelectExpression type = new TypeSelectExpression(
                exp.getType().getReferredType());        
        plainSelect.setType(type);

        ResSelectExpression res = new ResSelectExpression(
                new Column(tmpDmnAlias.concat(".res")));
        plainSelect.setRes(res);

        VarSelectExpression v = new VarSelectExpression(varName);
        v.setRefExpression(new Column(tmpDmnAlias.concat(".res")));
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
            refVEqId.setLeftExpression(
                    new Column(Arrays.asList(tmpObjAlias, "ref_"
                            .concat(varExp.getVariable().getName()))));
            refVEqId.setRightExpression(new Column(
                    Arrays.asList(className, className.concat("_id"))));
        } else {
            throw new OclException(
                    "Cannot parse non Variable expression");
        }
            
        BinaryExpression valEq1 = new EqualsTo();
        valEq1.setLeftExpression(
                new Column(Arrays.asList(tmpObjAlias, "val")));
        valEq1.setRightExpression(new LongValue(1L));

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
            refVEqOppos.setLeftExpression(
                    new Column(Arrays.asList(tmpObjAlias, "ref_"
                            .concat(varExp.getVariable().getName()))));
            refVEqOppos.setRightExpression(new Column(
                    Arrays.asList(assoc, oppos)));
        } else {
            throw new OclException(
                    "Cannot parse non Variable expression");
        }
            
        BinaryExpression valEq1 = new EqualsTo();
        valEq1.setLeftExpression(
                new Column(Arrays.asList(tmpObjAlias, "val")));
        valEq1.setRightExpression(new LongValue(1L));
        
        BinaryExpression onExp = new AndExpression(refVEqOppos, valEq1);
        leftJ.setOnExpression(onExp);
        
        plainSelect.setJoins(Arrays.asList(leftJ));
        
        return plainSelect;
    }
}








































