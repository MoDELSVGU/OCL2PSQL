/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class RealLiteralExp
 */
public class RealLiteralExp extends NumericLiteralExp {
    /**
     * Attribute realSymbol
     */
    private final double realSymbol;

    public RealLiteralExp(double realSymbol) {
	this.realSymbol = realSymbol;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	return this.realSymbol;
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	
}
