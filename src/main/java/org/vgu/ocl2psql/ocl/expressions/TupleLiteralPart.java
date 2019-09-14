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
 * Class TupleLiteralPart
 */
public class TupleLiteralPart {

    protected final OclExpression key;
    protected final OclExpression item;

    public TupleLiteralPart(OclExpression key, OclExpression item) {
	this.key = key;
	this.item = item;
    }

}
