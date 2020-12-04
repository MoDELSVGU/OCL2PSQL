/**************************************************************************
Copyright 2020 ngpbh
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author: ngpbh
***************************************************************************/

package org.vgu.ocl2psql.sql.utils;

import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.sql.statement.select.PlainSelect;
import org.vgu.ocl2psql.sql.statement.select.VarSelectExpression;

import com.vgu.se.jocl.expressions.Variable;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class VariableUtils {
    public static void addVar(List<Variable> vList, PlainSelect plainSelect,
        String tableAlias) {

        for (Variable v : vList) {
            VarSelectExpression varExp = new VarSelectExpression(v.getName());

            Column refCol = new Column(Arrays.asList(tableAlias,
                varExp.getRef().getAlias().getName()));

            varExp.setRefExpression(refCol);
            plainSelect.addVar(varExp);
        }

    }

    public static void addVarToList(List<Variable> vList,
        List<Expression> expressions, String tableAlias) {

        for (Variable v : vList) {
            VarSelectExpression varExp = new VarSelectExpression(v.getName());

            Expression refCol = new Column(Arrays.asList(tableAlias,
                varExp.getRef().getAlias().getName()));

            expressions.add(refCol);
        }
    }
}
