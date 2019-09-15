/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

/**
 * Class LoopExp
 */
public abstract class LoopExp extends CallExp {
    
    /**
     * Attribute body
     */
    private final OclExpression body;

    /**
     * Attribute iterator
     */
    private final Variable iterator;

    public LoopExp(OclExpression source, Variable iterator, OclExpression body) {
	super(source);
	this.iterator = iterator;
	this.body = body;
    }

    public Variable getIterator() {
	return iterator;
    }

    public OclExpression getBody() {
	return body;
    }

}
