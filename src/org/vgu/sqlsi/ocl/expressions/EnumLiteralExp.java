/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.sqlsi.ocl.expressions;

import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class EnumLiteralExp
 */
public final class EnumLiteralExp extends LiteralExp {

    private final TypeExp type;
    private final String referredLiteral;

    public EnumLiteralExp(TypeExp type, String referredLiteral) {
	this.type = type;
	this.referredLiteral = referredLiteral;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Class type = (Class) this.type.eval(context);
	if (type == null)
	    return null;
	if (type.isEnum()) {
	    return Enum.valueOf(type, referredLiteral);
	}
	throw new OclEvaluationException("Not an Enum Type: "
		+ this.type.referredType);
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }


}
