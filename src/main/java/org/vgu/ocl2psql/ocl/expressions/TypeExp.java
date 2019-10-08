/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import org.vgu.ocl2psql.ocl.deparser.DeparserVisitor;

/**
 * Class TypeExp
 */
public final class TypeExp implements OclExpression {
    protected final String referredType;

    public String getReferredType() {
		return referredType;
	}

	public TypeExp(String referredType) {
        this.referredType = referredType;
    }
	
	@Override
	public void accept(DeparserVisitor visitor) {
	    visitor.visit( this );
	}

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

	

}
