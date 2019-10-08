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
 * Class OperationCallExp
 */
public final class OperationCallExp extends FeatureCallExp {

    private final String name;

    private final List<OclExpression> arguments;

    public String getName() {
        return name;
    }

    public List<OclExpression> getArguments() {
        return arguments;
    }

    public OperationCallExp(OclExpression source, String name, OclExpression... arguments) {
        super(source);
        this.name = name;
        this.arguments = Arrays.asList(arguments);
    }

    @Override
    public void accept(RobertStmVisitor visitor) {
        visitor.visit(this);
    }
}
