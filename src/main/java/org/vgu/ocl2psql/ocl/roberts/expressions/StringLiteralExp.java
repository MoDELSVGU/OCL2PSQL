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
 * Class StringLiteralExp
 */
public class StringLiteralExp extends PrimitiveLiteralExp {
    /**
     * Attribute stringSymbol
     */
    private final String stringSymbol;

    public StringLiteralExp(String newstringSymbol) {
        this.stringSymbol = newstringSymbol;
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

    public String getStringSymbol() {
        return stringSymbol;
    }

    @Override
    public void accept(DeparserVisitor visitor) {
        visitor.visit(this);
    }

}
