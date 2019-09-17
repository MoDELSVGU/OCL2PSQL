package org.vgu.ocl2psql.ocl.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.vgu.ocl2psql.ocl.context.CascadingOclContext;
import org.vgu.ocl2psql.ocl.context.OclContext;
import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;
import org.vgu.ocl2psql.ocl.expressions.OclExpression;

public class OclIteratorSupport {

    public static Boolean exists(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);
	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    if ((Boolean) body.eval(innerContext))
		return true;
	}
	return false;
    }

    public static Boolean forAll(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);
	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    if (!(Boolean) body.eval(innerContext))
		return false;
	}
	return true;
    }
    
    public static Collection<Object> collect(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	List<Object> list = new ArrayList<Object>();
	CascadingOclContext innerContext = new CascadingOclContext(context);

	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    list.add(body.eval(innerContext));
	}
	return list;
    }

    public static Collection<Object> select(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {

	CascadingOclContext innerContext = new CascadingOclContext(context);
	if (source instanceof Set) {
	    Set<Object> set = new HashSet<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    SortedSet<Object> set = new TreeSet<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else {
	    List<Object> list = new ArrayList<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    list.add(object);
	    }
	    return list;
	}
    }

    
    public static Collection<Object> reject(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);

	if (source instanceof Set) {
	    Set<Object> set = new HashSet<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    SortedSet<Object> set = new TreeSet<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else {
	    List<Object> list = new ArrayList<Object>();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    list.add(object);
	    }
	    return list;
	}
    }

    
    public static Collection<Object> sortedBy(Collection<Object> source,
	    final OclContext context, final String iteratorName,
	    final OclExpression body) throws OclEvaluationException {
	try {
	    SortedSet<Object> set = new TreeSet<Object>(new Comparator<Object>() {

		@SuppressWarnings("unchecked")
        public int compare(Object o1, Object o2) {
		    try {
			CascadingOclContext innerContext = new CascadingOclContext(
				context);

			innerContext.setVariable(iteratorName, o1);
			Object r1 = body.eval(innerContext);
			innerContext.setVariable(iteratorName, o2);
			Object r2 = body.eval(innerContext);
			if (r1 instanceof Comparable) {
			    return ((Comparable<Object>) r1).compareTo(r2);
			} else {
			    return 0;
			}
		    } catch (OclEvaluationException e) {
			throw new RuntimeException(e);
		    }
		}
	    });
	    set.addAll(source);
	    return set;
	} catch (RuntimeException e) {
	    if (e.getCause() instanceof OclEvaluationException)
		throw (OclEvaluationException) e.getCause();
	    else
		throw new OclEvaluationException(e);
	}
    }

    public static Object any(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	Collection<Object> anys = select(source, context, iteratorName, body);
	if (anys.isEmpty())
	    return null;
	else
	    return anys.iterator().next();
    }

    public static Boolean one(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	return select(source, context, iteratorName, body).size() == 1;
    }

    
    public static Boolean isUnique(Collection<Object> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);
	Set<Object> set = new HashSet<Object>();
	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    if (!set.add(body.eval(innerContext)))
		return false;
	}
	return true;
    }

}
