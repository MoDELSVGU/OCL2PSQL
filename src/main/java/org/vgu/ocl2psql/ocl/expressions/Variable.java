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

/**
 * Class Variable
 */
public class Variable {
    protected final OclExpression initExpression;

    private final String name;
    
    protected final TypeExp type;

    public Variable(String name, TypeExp type, OclExpression initExpression) {
	this.name = name;
	this.initExpression = initExpression;
	this.type = type;
    }

    public OclExpression getInitExpression() {
		return initExpression;
	}

	public TypeExp getType() {
		return type;
	}

	public Variable(String name, TypeExp type) {
	this(name, type, null);
    }
    
    public Variable(String name) {
	this(name, null);
    }

    public Object init(OclContext context) throws OclEvaluationException {
	if (initExpression != null) {
	    return initExpression.eval(context);
	} else
	if (type != null) {
	    try {
		return ((Class<?>)type.eval(context)).newInstance();
	    } catch (Exception e) {
		throw new OclEvaluationException("cannot instantiate the type: "+type.referredType);
	    }
	} else {
	    return new Object();
	}
    }

    public String getName() {
	return name;
    }
}
