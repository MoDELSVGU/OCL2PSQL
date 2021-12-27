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
import com.vgu.se.jocl.visit.ParserVisitable;

public abstract class Expression implements ParserVisitable {
    protected Type type;
    protected String oclStr;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getOclStr() {
        return oclStr;
    }

    public void setOclStr(String oclStr) {
        this.oclStr = oclStr;
    }

    @Override
    public String toString() {
        return "Expression String : " + oclStr + "\n"
                + "Type : " + type + "\n";
    }
    
}
