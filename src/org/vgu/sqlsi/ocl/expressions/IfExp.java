/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class IfExp
 */
public class IfExp extends OclExpression {
    /**
     * Attribute condition
     */
    private final OclExpression condition;
    /**
     * Attribute elseExpression
     */
    private final OclExpression elseExpression;
    /**
     * Attribute thenExpression
     */
    private final OclExpression thenExpression;

    public IfExp(OclExpression condition, OclExpression thenExpression,
	    OclExpression elseExpression) {
	this.condition = condition;
	this.thenExpression = thenExpression;
	this.elseExpression = elseExpression;
    }

    public IfExp(OclExpression condition, OclExpression thenExpression) {
	this(condition, thenExpression, null);
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Object condition = this.condition.eval(context);
	if (condition instanceof Boolean) {
	    if ((Boolean) condition) {
		return this.thenExpression.eval(context);
	    } else if (this.elseExpression != null) {
		return this.elseExpression.eval(context);
	    } else {
		return null;
	    }
	} else {
	    throw new OclEvaluationException("Condition is not a boolean!");
	}
    }

	@Override
	public Statement accept(StmVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	
}
