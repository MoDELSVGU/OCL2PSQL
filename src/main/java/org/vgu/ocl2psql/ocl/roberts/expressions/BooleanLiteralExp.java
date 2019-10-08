/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.deparser.DeparserVisitor;
import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;

/**
 * Class BooleanLiteralExp
 */
public final class BooleanLiteralExp extends PrimitiveLiteralExp {
    /**
     * Attribute booleanSymbol
     */
    private final boolean booleanSymbol;

    public BooleanLiteralExp(boolean newbooleanSymbol) {
        this.booleanSymbol = newbooleanSymbol;
    }

    @Override
    public void accept(DeparserVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

    public boolean isBooleanSymbol() {
        return booleanSymbol;
    }

}
