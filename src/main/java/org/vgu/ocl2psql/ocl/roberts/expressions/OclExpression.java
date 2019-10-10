/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitable;
import org.vgu.ocl2psql.ocl.type.CollectionType;
import org.vgu.ocl2psql.ocl.type.SingleType;
import org.vgu.ocl2psql.ocl.type.Type;

/**
 * Class OclExpression
 */
public abstract class OclExpression implements RobertStmVisitable{
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public Type getElementType() {
        if(type == null || type instanceof SingleType)
            return type;
        return ((CollectionType<?>) type).getElementType();
    }
    
    public Type getLeafType() {
        return getLeafType(type);
    }

    private Type getLeafType(Type type) {
        if(type == null || type instanceof SingleType)
            return type;
        else
            return getLeafType(((CollectionType<?>) type).getElementType());
    }

}
