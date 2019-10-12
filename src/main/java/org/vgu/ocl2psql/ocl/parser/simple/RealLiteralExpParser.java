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

import com.vgu.se.jocl.expressions.RealLiteralExp;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;

public class RealLiteralExpParser extends SimpleOclParser {

    private Select select;
    private PlainSelect plainSelect;

    public RealLiteralExpParser() {
        super();
        this.plainSelect = new PlainSelect();
    }

    @Override
    public void visit(RealLiteralExp realLiteralExp) {
        plainSelect.createTrueValColumn();

        ResSelectExpression res = new ResSelectExpression(
                new LongValue(realLiteralExp.getValue().toString()));
        plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(
                realLiteralExp.getType().getReferredType());
        plainSelect.setType(type);

        Select select = new Select();
        select.setSelectBody(plainSelect);

        this.setSelect(select);
    }

}
