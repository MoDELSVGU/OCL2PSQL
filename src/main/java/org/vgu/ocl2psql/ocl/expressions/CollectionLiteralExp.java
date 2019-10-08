/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import java.util.Arrays;
import java.util.List;

import org.vgu.ocl2psql.ocl.deparser.DeparserVisitor;

/**
 * Class CollectionLiteralExp
 */
public final class CollectionLiteralExp extends LiteralExp {

    protected final List<CollectionLiteralPart> parts;
    protected final CollectionKind kind;

    public CollectionLiteralExp(CollectionKind kind, CollectionLiteralPart... parts) {
        this.kind = kind;
        this.parts = Arrays.asList(parts);
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
