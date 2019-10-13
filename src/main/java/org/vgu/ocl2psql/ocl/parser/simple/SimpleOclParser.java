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

import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.IntegerLiteralExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.LiteralExp;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.RealLiteralExp;
import com.vgu.se.jocl.expressions.StringLiteralExp;
import com.vgu.se.jocl.visit.ParserVisitor;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

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
    }

    @Override
    public void visit(OperationCallExp operationCallExp) {
        PlainSelect plainSelect = parse(operationCallExp);

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
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(AssociationClassCallExp associationClassCallExp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringLiteralExp stringLiteralExp) {
        PlainSelect plainSelect = parse(stringLiteralExp);

        addComment(stringLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(BooleanLiteralExp booleanLiteralExp) {
        PlainSelect plainSelect = parse(booleanLiteralExp);

        addComment(booleanLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(IntegerLiteralExp integerLiteralExp) {
        PlainSelect plainSelect = parse(integerLiteralExp);

        addComment(integerLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
        PlainSelect plainSelect = parse(realLiteralExp);

        addComment(realLiteralExp, plainSelect);

        this.select = new Select();
        this.select.setSelectBody(plainSelect);
    }
    
    //***********************************************
    //* Helpers
    //***********************************************

    private PlainSelect parse(BooleanLiteralExp exp) {
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

    private PlainSelect parse(IntegerLiteralExp exp) {
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

    private PlainSelect parse(RealLiteralExp exp) {
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

    private PlainSelect parse(StringLiteralExp exp) {
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

    private PlainSelect parse(OperationCallExp exp) {

        switch (exp.getReferredOperation().getName()) {
        case "allInstances":
            return parseOpCallAllInstances(exp);
        default:
            return null;
        }
    }

    private PlainSelect parseOpCallAllInstances(OperationCallExp exp) {
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
}
