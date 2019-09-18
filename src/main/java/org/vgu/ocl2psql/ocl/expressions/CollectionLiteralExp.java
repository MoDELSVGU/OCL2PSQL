/*
 * Copyright (c) 2009 by Robert Herschke,
 * Hauptstrasse 30, 65760 Eschborn, rh@ocl.herschke.de.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Robert Herschke. ("Confidential Information").
 */
package org.vgu.ocl2psql.ocl.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;

import net.sf.jsqlparser.statement.Statement;

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
    public Object eval(OclContext context) throws OclEvaluationException {
	Collection<Object> collection;
	switch (kind) {
	case Set:
	    collection = new HashSet<Object>();
	    break;
	case OrderedSet:
	    collection = new TreeSet<Object>();
	    break;
	case Bag:
	case Sequence:
	default:
	    collection = new ArrayList<Object>();
	    break;
	}
	for (CollectionLiteralPart part : parts) {
	    if (part instanceof CollectionItem) {
		collection.add(((CollectionItem) part).item.eval(context));
	    } else {
		throw new OclEvaluationException(
			"Collection ranges are not yet supported");
	    }
	}
	return collection;
    }

    @Override
    public Statement map(StmVisitor visitor) {
        // TODO Auto-generated method stub
        return null;
    }

	
}
