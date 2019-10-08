/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitor;

/**
 * Class NullLiteralExp
 */
public class NullLiteralExp extends LiteralExp {

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
