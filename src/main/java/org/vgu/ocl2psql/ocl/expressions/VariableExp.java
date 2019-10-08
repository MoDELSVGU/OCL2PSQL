/*
// * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import org.vgu.ocl2psql.ocl.deparser.DeparserVisitor;

/**
 * Class VariableExp
 */
public class VariableExp implements OclExpression {
    /**
     * Attribute referredVariable
     */
    private final Variable referredVariable;

    public VariableExp(Variable referredVariable) {
	this.referredVariable = referredVariable;
    }

	public Variable getReferredVariable() {
		return referredVariable;
	}
	
	@Override
	public void accept( DeparserVisitor visitor ) {
	    visitor.visit( this );
	}

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
