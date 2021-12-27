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

package com.vgu.se.jocl.visit;

import com.vgu.se.jocl.expressions.AssociationClassCallExp;
import com.vgu.se.jocl.expressions.BooleanLiteralExp;
import com.vgu.se.jocl.expressions.Expression;
import com.vgu.se.jocl.expressions.IntegerLiteralExp;
import com.vgu.se.jocl.expressions.IteratorExp;
import com.vgu.se.jocl.expressions.LiteralExp;
import com.vgu.se.jocl.expressions.OperationCallExp;
import com.vgu.se.jocl.expressions.PropertyCallExp;
import com.vgu.se.jocl.expressions.StringLiteralExp;
import com.vgu.se.jocl.expressions.VariableExp;

public interface ParserVisitor {

    void visit(Expression exp);

    void visit(IteratorExp iteratorExp);

    void visit(OperationCallExp operationCallExp);

    void visit(LiteralExp literalExp);

    void visit(StringLiteralExp stringLiteralExp);

    void visit(BooleanLiteralExp booleanLiteralExp);

    void visit(IntegerLiteralExp integerLiteralExp);

    void visit(PropertyCallExp propertyCallExp);

    void visit(AssociationClassCallExp associationClassCallExp);

    void visit(VariableExp variableExp);

//        void visit(RealLiteralExp realLiteralExp);
//        void visit(SqlFnCurdate variableExp);
//        void visit(SqlFnTimestampdiff variableExp);

}
