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
***************************************************************************/

package org.vgu.ocl2psql.ocl.roberts.visitor;

import org.vgu.ocl2psql.ocl.roberts.expressions.BooleanLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.CollectionLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.EnumLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IfExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IntegerLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.InvalidLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IterateExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.IteratorExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.LetExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.NullLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.OperationCallExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.PropertyCallExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.RealLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.StringLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.TupleLiteralExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.TypeExp;
import org.vgu.ocl2psql.ocl.roberts.expressions.VariableExp;

public interface RobertStmVisitor {
    void visit(PropertyCallExp propertyCallExp);

    void visit(OperationCallExp operationCallExp);

    void visit(IterateExp iterateExp);

    void visit(IteratorExp iteratorExp);

    void visit(IfExp ifExp);

    void visit(LetExp letExp);

    void visit(CollectionLiteralExp collectionLiteralExp);

    void visit(EnumLiteralExp enumLiteralExp);

    void visit(InvalidLiteralExp invalidLiteralExp);

    void visit(NullLiteralExp nullLiteralExp);

    void visit(BooleanLiteralExp booleanLiteralExp);

    void visit(IntegerLiteralExp integerLiteralExp);

    void visit(RealLiteralExp realLiteralExp);

    void visit(StringLiteralExp stringLiteralExp);

    void visit(TupleLiteralExp tupleLiteralExp);

    void visit(TypeExp typeExp);

    void visit(VariableExp variableExp);
}
