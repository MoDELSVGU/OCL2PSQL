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


package com.vgu.se.jocl.expressions;

import com.vgu.se.jocl.types.Type;
import com.vgu.se.jocl.visit.ParserVisitor;

public class TypeExp extends OclExp {

    private Type referredType;

    public TypeExp(String referredType) {
        this.referredType = new Type(referredType);
        super.setType(new Type(referredType));
    }
    
    public TypeExp(Type type) {
        this.referredType = type;
    }
    
    public Type getType() {
        return referredType;
    }

    @Override
    public void accept(ParserVisitor parserVisitor) {
        parserVisitor.visit(this);
    }
    
}
