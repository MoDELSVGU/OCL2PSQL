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

import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.vgu.ocl2psql.ocl.parser.Ocl2PsqlSvc;
import org.vgu.ocl2psql.ocl.roberts.expressions.TypeExp;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.utils.SQLAsStringUtils;

import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.expressions.OclExp;
import com.vgu.se.jocl.expressions.Variable;
import com.vgu.se.jocl.parser.simple.SimpleParser;
import com.vgu.se.jocl.types.Type;

import net.sf.jsqlparser.statement.select.SelectItem;

public class SimpleO2PApi extends Ocl2PsqlSvc {

    private JSONArray ctx;
    
    private SimpleParser parser = new SimpleParser();
    private SimpleOclParser o2pParser = new SimpleOclParser();

    public String mapToString(String oclExp) {
        Select finalStatement = this.mapToSQL(oclExp);

        if (descriptionMode == true) {
            String finalStatementString = finalStatement
                    .toStringWithDescription();

            return SQLAsStringUtils.applyIndent(finalStatementString);

        } else {
            return finalStatement.toString();
        }
    }

    @Override
    public Select mapToSQL(String oclExp) {

        Expression newExp = parser.parse(oclExp, ctx);

        o2pParser.setCtx(ctx);
        newExp.accept(o2pParser);

        return cookFinalStatement(o2pParser.getSelect());
    }

    @Override
    public void setContext(JSONArray ctx) {
        this.ctx = ctx;
    }

    @Override
    public void setContextualType(String varName, String varType) {
        parser.putAdhocContextualSet(
                new Variable(varName, new Type(varType)));
    }

    private Select cookFinalStatement(Select finalStatement) {
        PlainSelect finalPlainSelect = (PlainSelect) finalStatement
                .getSelectBody();

        List<SelectItem> newSelectItems = new ArrayList<SelectItem>();

        for (SelectItem item : finalPlainSelect.getSelectItems()) {
            if (item instanceof ResSelectExpression
                    || item instanceof ValSelectExpression
                    || item instanceof TypeSelectExpression) {
                newSelectItems.add(item);
            }
        }

        finalPlainSelect.getSelectItems().clear();
        finalPlainSelect.getSelectItems().addAll(newSelectItems);

        return finalStatement;
    }

}
