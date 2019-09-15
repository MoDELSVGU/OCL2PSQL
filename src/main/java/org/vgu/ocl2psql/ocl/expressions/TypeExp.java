/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class TypeExp
 */
public final class TypeExp extends OclExpression {
    protected final String referredType;

    public String getReferredType() {
		return referredType;
	}

	public TypeExp(String referredType) {
	this.referredType = referredType;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	if ("String".equals(this.referredType)) {
	    return String.class;
	} else if ("Boolean".equals(this.referredType)) {
	    return Boolean.class;
	} else if ("Integer".equals(this.referredType)) {
	    return Integer.class;
	} else if ("Real".equals(this.referredType)) {
	    return Double.class;
	} else if ("OclVoid".equals(this.referredType)) {
	    return Void.class;
	} else if ("OclAny".equals(this.referredType)) {
	    return Object.class;
	} else {
	    return context.getType(this.referredType);
	}
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	

}
