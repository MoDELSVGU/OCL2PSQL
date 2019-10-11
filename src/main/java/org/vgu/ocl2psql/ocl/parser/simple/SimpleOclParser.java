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
import org.vgu.ocl2psql.sql.statement.select.Select;

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

public class SimpleOclParser implements ParserVisitor {
    
    private Select select;

    public Select getSelect() {
        return this.select;
    }
    
    public void setSelect(Select select) {
        this.select = select;
    }
    
    public <E extends OclExp> void addComment(E exp, Select select){
        ((PlainSelect) select.getSelectBody())
                .setCorrespondOCLExpression(exp.getOclStr());
        ;
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
    public void visit(PropertyCallExp propertyCallExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(AssociationClassCallExp associationClassCallExp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(StringLiteralExp stringLiteralExp) {
        StringLiteralExpParser parser = new StringLiteralExpParser();
        stringLiteralExp.accept(parser);

        addComment(stringLiteralExp, parser.getSelect());

        this.select = parser.getSelect();
    }

    @Override
    public void visit(BooleanLiteralExp booleanLiteralExp) {
        BooleanLiteralExpParser parser = new BooleanLiteralExpParser();
        booleanLiteralExp.accept(parser);

        addComment(booleanLiteralExp, parser.getSelect());

        this.select = parser.getSelect();
    }

    @Override
    public void visit(IntegerLiteralExp integerLiteralExp) {
        IntegerLiteralExpParser parser = new IntegerLiteralExpParser();
        integerLiteralExp.accept(parser);

        addComment(integerLiteralExp, parser.getSelect());

        this.select = parser.getSelect();
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
        RealLiteralExpParser parser = new RealLiteralExpParser();
        realLiteralExp.accept(parser);

        addComment(realLiteralExp, parser.getSelect());

        this.select = parser.getSelect();
    }

}
