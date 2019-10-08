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


package org.vgu.ocl2psql.ocl.roberts.deparser;

import org.vgu.ocl2psql.ocl.roberts.expressions.TypeExp;

public class TypeExpDeParser extends OclExpressionDeParser {
    private String deParsedStr;

    public TypeExpDeParser() {
        this( "" );
    }
    
    public TypeExpDeParser(String deParsedStr) {
        this.deParsedStr = deParsedStr;
    }

    public String getDeParsedString() {
        return deParsedStr;
    }

    @Override
    public void visit(TypeExp typeExp) {
        String referredType = typeExp.getReferredType();
        this.deParsedStr = this.deParsedStr.concat( referredType );
    }

}
