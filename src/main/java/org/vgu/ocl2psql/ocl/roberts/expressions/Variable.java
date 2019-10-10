/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

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

    public String getName() {
	return name;
    }
}
