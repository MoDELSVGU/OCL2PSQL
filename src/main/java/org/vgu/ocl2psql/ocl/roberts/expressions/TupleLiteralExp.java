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
 * Class TupleLiteralExp
 */
public final class TupleLiteralExp extends LiteralExp {

    private final List<TupleLiteralPart> parts;

    public TupleLiteralExp(TupleLiteralPart... parts) {
	this.parts = Arrays.asList(parts);
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
