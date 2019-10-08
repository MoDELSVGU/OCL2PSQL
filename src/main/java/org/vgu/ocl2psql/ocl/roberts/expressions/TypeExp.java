/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;

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
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
