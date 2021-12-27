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

import java.util.List;

import com.vgu.se.jocl.exception.OclParserException;
import com.vgu.se.jocl.types.Type;
import com.vgu.se.jocl.visit.ParserVisitor;

public class OperationCallExp extends FeatureCallExp {

    private List<Expression> arguments;
    private Operation referredOperation;

//    public OperationCallExp(OclExp source, Operation referredOperation, 
//            OclExp... arguments) {
//        super.source = source;
//        this.referredOperation = referredOperation;
//        this.arguments = Arrays.asList(arguments);
//    }

    public OperationCallExp(Expression source, Operation referredOperation, 
            List<Expression> arguments) {
        super.source = source;
        this.referredOperation = referredOperation;
        this.arguments = arguments;
        super.setType(getOperationExpType());
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public Expression getSource () {
        return super.source;
    }

    public Operation getReferredOperation() {
        return referredOperation;
    }
    
    @Override
    public void accept(ParserVisitor parserVisitor) {
        parserVisitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "referredOperation : " + this.referredOperation.getName()
                + "\n" 
                + "type : " + super.getType() + "\n" 
                + "source : " + super.source + "\n" 
                + "arguments : " + this.arguments + "\n";
    }
    
    private Type getOperationExpType() {
        Type opType = new Type();
        switch (referredOperation.getName()) {
        case "allInstances":
            String leftExpType = source.getType().getReferredType();
            opType = new Type("Col(" + leftExpType + ")");
            return opType;
        case "flatten":
            return source.getType();
        case "size":
            opType = new Type("Integer");
            return opType;
        case "isEmpty":
        case "notEmpty":
        case "isUnique":
        case "not":
        case "=":
        case "<>":
        case "<":
        case ">":
        case ">=":
        case "<=":
        case "and":
        case "or":
        case "oclIsUndefined":
        case "oclIsKindOf":
        case "oclIsTypeOf":
            opType = new Type("Boolean");
            return opType;
//        case "oclAsType":
//            String argType = exps[0].getType().getReferredType();
//            if (!UMLContextUtils.isSuperClassOf(this.dm, leftExpType,
//                    argType)) {
//                throw new OclParserException("\n======\n"
//                        + "Cannot perform casting!");
//            }
//            opType = new Type(argType);
//            return opType;
        default:
            throw new OclParserException(
                "\n======\n" + referredOperation.getName() + " not supported!");
        }
    }

}
