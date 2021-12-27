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

@author: ngpbh
***************************************************************************/

package com.vgu.se.jocl.expressions;

import com.vgu.se.jocl.types.Class;
import com.vgu.se.jocl.types.Type;
import com.vgu.se.jocl.visit.ParserVisitor;;

public class AssociationClassCallExp extends NavigationCallExp {

    private String referredAssociationEnd;
    private Class referredAssociationClass;
    private String oppositeAssociationEnd;
    private Type oppositeAssociationEndType;
    private Type referredAssociationEndType;
    private String association;

    public AssociationClassCallExp(Expression source,
            String referredAssociationEnd) {
        super.navigationSource = source;
        this.referredAssociationEnd = referredAssociationEnd;
    }

    @Override
    public void accept(ParserVisitor parserVisitor) {
        parserVisitor.visit(this);
    }

    public String getReferredAssociationEnd() {
        return referredAssociationEnd;
    }

    public void setReferredAssociationEnd(String referredAssociationEnd) {
        this.referredAssociationEnd = referredAssociationEnd;
    }

    public Class getReferredAssociationClass() {
        return referredAssociationClass;
    }

    public void setReferredAssociationClass(Class referredAssociationClass) {
        this.referredAssociationClass = referredAssociationClass;
    }

    public String getOppositeAssociationEnd() {
        return oppositeAssociationEnd;
    }

    public void setOppositeAssociationEnd(String oppositeAssociationEnd) {
        this.oppositeAssociationEnd = oppositeAssociationEnd;
    }

    public Type getOppositeAssociationEndType() {
        return oppositeAssociationEndType;
    }

    public void setOppositeAssociationEndType(Type oppositeAssociationEndType) {
        this.oppositeAssociationEndType = oppositeAssociationEndType;
    }

    public Type getReferredAssociationEndType() {
        return referredAssociationEndType;
    }

    public void setReferredAssociationEndType(Type referredAssociationEndType) {
        this.referredAssociationEndType = referredAssociationEndType;
    }

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }
    
    public void parseAssociationName() {
        String leftType = referredAssociationEndType.getReferredType();
        String rightType = oppositeAssociationEndType.getReferredType();
        if(leftType.compareTo(rightType) > 0) {
            this.association = String.format("%1$s_%2$s_%3$s_%4$s", rightType,
                oppositeAssociationEnd, referredAssociationEnd, leftType);
        } else {
            this.association = String.format("%1$s_%2$s_%3$s_%4$s", leftType,
                referredAssociationEnd, oppositeAssociationEnd, rightType);
        }
    }
    
}
