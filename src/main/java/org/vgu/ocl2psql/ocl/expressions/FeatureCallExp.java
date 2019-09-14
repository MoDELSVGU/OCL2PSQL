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
 * Class FeatureCallExp
 */
public abstract class FeatureCallExp extends CallExp {

    public FeatureCallExp(OclExpression newsource) {
	super(newsource);
    }

}
