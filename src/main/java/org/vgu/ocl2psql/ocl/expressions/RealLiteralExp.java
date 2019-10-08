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
import org.vgu.ocl2psql.sql.statement.select.Select;

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
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(DeparserVisitor visitor) {
        // TODO Auto-generated method stub
        
    }

	
}
