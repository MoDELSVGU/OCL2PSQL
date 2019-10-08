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
 * Class IfExp
 */
public class IfExp implements OclExpression {
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

    public IfExp(OclExpression condition, OclExpression thenExpression, OclExpression elseExpression) {
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    public IfExp(OclExpression condition, OclExpression thenExpression) {
        this(condition, thenExpression, null);
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(DeparserVisitor visitor) {
        visitor.visit(this);
    }

}
