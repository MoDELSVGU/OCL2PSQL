/*
// * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;

/**
 * Class VariableExp
 */
public class VariableExp extends OclExpression {
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
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
