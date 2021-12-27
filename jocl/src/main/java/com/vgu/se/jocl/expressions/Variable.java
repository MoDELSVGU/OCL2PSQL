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

public class Variable {
    private Expression initExp;
    private String name;
    private Type type;
    private Expression source;
    
    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Variable(Expression source, String name, Type type) {
        this.source = source;
        this.name = name;
        this.type = type;
    }
    
    public Variable(String name, Expression source) {
        this.source = source;
        this.name = name;
        this.type = source.getType().getElementType();
    }

    public Expression getInitExp() {
        return initExp;
    }

    public String getName() {
        return name;
    }
    
    public Type getType() {
        return type;
    }

    public void setInitExp(OclExp initExp) {
        this.initExp = initExp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public Expression getSource() {
        return source;
    }

    public void setSource(Expression source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Variable : " + name + " -- Type : " + type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variable other = (Variable) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
