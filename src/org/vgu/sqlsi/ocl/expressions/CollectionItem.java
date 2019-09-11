/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

/**
 * Class CollectionItem
 */
public final class CollectionItem extends CollectionLiteralPart {

    protected final OclExpression item;

    public CollectionItem(OclExpression item) {
	this.item = item;
    }

}
