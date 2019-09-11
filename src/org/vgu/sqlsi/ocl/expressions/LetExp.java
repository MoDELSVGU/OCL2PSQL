/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import java.util.Arrays;
import java.util.List;

import org.vgu.sqlsi.ocl.context.CascadingOclContext;
import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.Statement;

/**
 * Class LetExp
 */
public final class LetExp extends OclExpression {
    private OclExpression in;

    private List<Variable> variables;

    public LetExp(OclExpression in, Variable... variables) {
	this.in = in;
	this.variables = Arrays.asList(variables);
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	// clone the context
	CascadingOclContext innerContext = new CascadingOclContext(context);
	for (Variable v : variables) {
	    // store the variable in the inner context
	    if (v.initExpression == null) {
		innerContext.setVariable(v.getName(), null);
	    } else {
		innerContext
			.setVariable(v.getName(), v.initExpression.eval(context));
	    }
	}
	// evaluate the let expression with the inner context
	return in.eval(innerContext);
    }

	@Override
	public Statement accept(StmVisitor visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
