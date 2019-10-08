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
 * Class CallExp
 */
public abstract class CallExp implements OclExpression {

    protected final OclExpression source;

    public CallExp(OclExpression source) {
	this.source = source;
    }

	public OclExpression getSource() {
		return source;
	}

}
