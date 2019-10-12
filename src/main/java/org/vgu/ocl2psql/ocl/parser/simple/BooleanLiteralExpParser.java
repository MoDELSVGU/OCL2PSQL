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

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectBody;

public class BooleanLiteralExpParser implements ParserVisitor {

    private Select select;
    private PlainSelect plainSelect;

    public BooleanLiteralExpParser() {
        this.plainSelect = new PlainSelect();
        this.select = new Select();
    }
    
    public Select getSelect() {
        return this.select;
    }

    @Override
    public void visit(BooleanLiteralExp booleanLiteralExp) {
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new Column(booleanLiteralExp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                booleanLiteralExp.getType().getReferredType());
        plainSelect.setType(type);

        select.setSelectBody(plainSelect);
    }

    @Override
    public void visit(OclExp oclExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(IteratorExp iteratorExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(OperationCallExp operationCallExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(LiteralExp literalExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(StringLiteralExp stringLiteralExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(IntegerLiteralExp integerLiteralExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
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

}
