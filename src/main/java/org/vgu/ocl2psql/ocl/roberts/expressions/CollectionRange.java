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
 * Class CollectionRange
 */
public final class CollectionRange extends CollectionLiteralPart {

    protected final OclExpression first;

    protected final OclExpression last;

    public CollectionRange(OclExpression first, OclExpression last) {
	super();
	this.first = first;
	this.last = last;
    }

}
