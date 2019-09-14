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
import java.util.Collection;
import java.util.Collections;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.ocl.impl.OclIterateSupport;

import net.sf.jsqlparser.statement.Statement;

/**
 * Class IterateExp
 */
public final class IterateExp extends LoopExp {

    private final Variable accumulator;

    public IterateExp(OclExpression source, Variable iterator,
	    Variable accumulator, OclExpression body) {
	super(source, iterator, body);
	this.accumulator = accumulator;
    }

    @Override
    public Object eval(OclContext context) throws OclEvaluationException {
	Object source = this.source.eval(context);
	Collection<?> collection;
	if (source == null) {
	    collection = Collections.emptyList();
	} else if (source.getClass().isArray()) {
	    collection = Arrays.asList((Object[])source);
	} else if (!(source instanceof Collection<?>)) {
	    collection = Collections.singletonList(source);
	} else {
	    collection = (Collection<?>) source;
	}
	String iteratorName = this.getIterator().getName();
	String accuName = this.accumulator.getName();
	Object initAccu = this.accumulator.init(context);
	return OclIterateSupport.iterate(collection, context, iteratorName,
		accuName, initAccu, this.getBody());
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	
}
