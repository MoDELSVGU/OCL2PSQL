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

package org.vgu.ocl2psql.ocl.roberts.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.vgu.ocl2psql.ocl.parser.O2P;
import org.vgu.ocl2psql.ocl.roberts.context.DefaultOclContext;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;
import org.vgu.ocl2psql.ocl.roberts.expressions.OclExpression;
import org.vgu.ocl2psql.ocl.type.SingleType;
import org.vgu.ocl2psql.ocl.type.Type;
import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.ResSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.Select;
import org.vgu.ocl2psql.sql.statement.select.TypeSelectExpression;
import org.vgu.ocl2psql.sql.statement.select.ValSelectExpression;
import org.vgu.ocl2psql.sql.utils.SQLAsStringUtils;

import net.sf.jsqlparser.statement.select.SelectItem;

public class LegacyO2PApi extends O2P {

    private JSONArray ctx;

    private RobertOCLParser ocl2sqlParser = new RobertOCLParser();

    @Override
    public String mapToString(String oclExpression) {
        Select finalStatement = this.mapToSQL(oclExpression);
        if (this.descriptionMode) {
            String finalStatementString = finalStatement
                    .toStringWithDescription();
            return SQLAsStringUtils.applyIndent(finalStatementString);
        } else {
            return finalStatement.toString();
        }
    }

    @Override
    public Select mapToSQL(String oclExpression) {
        ocl2sqlParser.resetLevelOfSet();
        ocl2sqlParser.resetVisitorContext();
        
        OclExpression exp = null;
        try {
            exp = RobertOcl2PojoParser.parse(oclExpression,
                    new DefaultOclContext());
        } catch (OclParseException e) {
            e.printStackTrace();
        }

        exp.accept(ocl2sqlParser);

        return cookFinalStatement(ocl2sqlParser.getFinalSelect());
    }

    @Override
    public void setContext(JSONArray ctx) {
        this.ctx = ctx;
        ocl2sqlParser.setPlainUMLContext(ctx);
    }

    @Override
    public void setContextualType(String typeName) {
        Type type = new SingleType(typeName);
        ocl2sqlParser.setContextualType(type);
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
