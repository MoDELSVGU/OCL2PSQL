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


package com.vgu.se.jocl.expressions.sql;

import java.util.List;
import java.util.Optional;

import com.vgu.se.jocl.expressions.Expression;

public abstract class SqlFunctionExp extends SqlExp {

    protected List<Expression> params;
    protected List<LiteralParam> literalParams;
    
    public SqlFunctionExp(String name) {
        super(name);
    }
    
    public SqlFunctionExp(String name, List<Expression> params, List<LiteralParam> literalParams) {
        super(name);
        this.params = params;
        this.literalParams = literalParams;
    }

    public List<Expression> getParams() {
        return params;
    }

    public List<LiteralParam> getLiteralParams() {
        return literalParams;
    }

    public void setLiteralParams(List<LiteralParam> literalParams) {
        this.literalParams = literalParams;
    }
    
    public int getParamCount() {
        return Optional.of(params).map(List::size).orElse(0)
            + Optional.of(literalParams).map(List::size).orElse(0);
    }

    @Override
    public String toString() {
        return "SqlFunctionExp [name =" + super.getName() + "]";
    }
    
}
