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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class TupleLiteralExp
 */
public final class TupleLiteralExp extends LiteralExp {

    private final List<TupleLiteralPart> parts;

    public TupleLiteralExp(TupleLiteralPart... parts) {
	this.parts = Arrays.asList(parts);
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Map<OclExpression, OclExpression> map = new HashMap<OclExpression, OclExpression>();
	for (TupleLiteralPart part : parts) {
	    map.put(part.key, part.item);
	}
	return map;
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	
}
