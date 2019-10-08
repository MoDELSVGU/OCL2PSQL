/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;

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
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
