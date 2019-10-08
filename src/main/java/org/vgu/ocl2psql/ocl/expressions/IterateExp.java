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

/**
 * Class IterateExp
 */
public final class IterateExp extends LoopExp {

    private final Variable accumulator;

    public IterateExp(OclExpression source, Variable iterator, Variable accumulator, OclExpression body) {
        super(source, iterator, body);
        this.accumulator = accumulator;
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
