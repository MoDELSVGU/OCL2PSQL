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
 * Class PropertyCallExp
 */
public final class PropertyCallExp extends NavigationCallExp {

    private final String name;

    public String getName() {
        return name;
    }

    public PropertyCallExp(OclExpression newsource, String name, OclExpression... qualifier) {
        super(newsource, qualifier);
        this.name = name;
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
