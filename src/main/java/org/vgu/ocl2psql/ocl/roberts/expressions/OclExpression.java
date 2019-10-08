/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.roberts.expressions;

import org.vgu.ocl2psql.ocl.roberts.visitor.RobertStmVisitable;

/**
 * Class OclExpression
 */
public abstract class OclExpression implements RobertStmVisitable{
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
