/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import java.util.Arrays;
import java.util.List;

/**
 * Class NavigationCallExp
 */
public abstract class NavigationCallExp extends FeatureCallExp {

    /**
     * Attribute qualifier
     */
    protected final List<OclExpression> qualifier;

    public NavigationCallExp(OclExpression newsource,
	    OclExpression... qualifier) {
	super(newsource);
	this.qualifier = Arrays.asList(qualifier);
    }

}
