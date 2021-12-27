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


package com.vgu.se.jocl.expressions.sql.functions;

import java.util.List;

import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.expressions.sql.LiteralParam;
import com.vgu.se.jocl.expressions.sql.SqlFunctionExp;
import com.vgu.se.jocl.types.Type;
import com.vgu.se.jocl.visit.ParserVisitable;
import com.vgu.se.jocl.visit.ParserVisitor;

public class SqlFnTimestampdiff extends SqlFunctionExp implements ParserVisitable {

    public SqlFnTimestampdiff(String name, List<Expression> params, List<LiteralParam> literalParams) {
        super(name, params, literalParams);
        super.setType(new Type("Integer"));
    }

    @Override
    public void accept(ParserVisitor visitor) {
        visitor.visit(this);
    }

}
