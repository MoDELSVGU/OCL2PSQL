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
 * Class IteratorExp
 */
public final class IteratorExp extends LoopExp {

    public final IteratorKind kind;

    public IteratorExp(OclExpression source, IteratorKind kind,
	    Variable iterator, OclExpression body) {
	super(source, iterator, body);
	this.kind = kind;
    }
    
    @Override
    public void accept( DeparserVisitor visitor ) {
        visitor.visit( this );
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }

}
