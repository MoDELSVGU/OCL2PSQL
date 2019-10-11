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

import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.OperationCallExp;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectBody;

public class OperationCallExpParser extends SimpleOclParser {

    private PlainSelect plainSelect;

    public OperationCallExpParser() {
        super();
        this.plainSelect = new PlainSelect();
    }

    @Override
    public void visit(OperationCallExp operationCallExp) {
        
        switch(operationCallExp.getReferredOperation().getName()) {
        case "allInstances":
            handleAllInstances(operationCallExp);
            break;
        }

        Select select = new Select();
        select.setSelectBody(this.plainSelect);

        super.setSelect(select);
    }
    
    private void handleAllInstances(OperationCallExp operationCallExp) {
        this.plainSelect.createTrueValColumn();
        
        String tableName = operationCallExp.getSource().getType()
                .getReferredType();

        ResSelectExpression res = new ResSelectExpression(
                new Column(tableName.concat("_id")));
        this.plainSelect.setRes(res);

        TypeSelectExpression type = new TypeSelectExpression(tableName);
        this.plainSelect.setType(type);
        
        Table table = new Table(tableName);
        this.plainSelect.setFromItem(table);
    }

}
