package org.vgu.sqlsi.ocl.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.vgu.sqlsi.ocl.context.CascadingOclContext;
import org.vgu.sqlsi.ocl.context.OclContext;
import org.vgu.sqlsi.ocl.exception.OclEvaluationException;
import org.vgu.sqlsi.ocl.expressions.OclExpression;

public class OclIteratorSupport {

    public static Boolean exists(Collection<?> source, OclContext context,
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

    public static Boolean forAll(Collection<?> source, OclContext context,
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
    
    @SuppressWarnings("unchecked")
    public static Collection collect(Collection source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	List list = new ArrayList();
	CascadingOclContext innerContext = new CascadingOclContext(context);

	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    list.add(body.eval(innerContext));
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    public static Collection select(Collection source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {

	CascadingOclContext innerContext = new CascadingOclContext(context);
	if (source instanceof Set) {
	    Set set = new HashSet();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    SortedSet set = new TreeSet();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else {
	    List list = new ArrayList();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if ((Boolean) body.eval(innerContext))
		    list.add(object);
	    }
	    return list;
	}
    }

    @SuppressWarnings("unchecked")
    public static Collection reject(Collection source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);

	if (source instanceof Set) {
	    Set set = new HashSet();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    SortedSet set = new TreeSet();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    set.add(object);
	    }
	    return set;
	} else {
	    List list = new ArrayList();
	    for (Object object : source) {
		innerContext.setVariable(iteratorName, object);
		if (!(Boolean) body.eval(innerContext))
		    list.add(object);
	    }
	    return list;
	}
    }

    @SuppressWarnings("unchecked")
    public static Collection sortedBy(Collection source,
	    final OclContext context, final String iteratorName,
	    final OclExpression body) throws OclEvaluationException {
	try {
	    SortedSet set = new TreeSet(new Comparator() {

		public int compare(Object o1, Object o2) {
		    try {
			CascadingOclContext innerContext = new CascadingOclContext(
				context);

			innerContext.setVariable(iteratorName, o1);
			Object r1 = body.eval(innerContext);
			innerContext.setVariable(iteratorName, o2);
			Object r2 = body.eval(innerContext);
			if (r1 instanceof Comparable) {
			    return ((Comparable) r1).compareTo(r2);
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

    public static Object any(Collection<?> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	Collection<?> anys = select(source, context, iteratorName, body);
	if (anys.isEmpty())
	    return null;
	else
	    return anys.iterator().next();
    }

    public static Boolean one(Collection<?> source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	return select(source, context, iteratorName, body).size() == 1;
    }

    @SuppressWarnings("unchecked")
    public static Boolean isUnique(Collection source, OclContext context,
	    String iteratorName, OclExpression body)
	    throws OclEvaluationException {
	CascadingOclContext innerContext = new CascadingOclContext(context);
	Set set = new HashSet();
	for (Object object : source) {
	    innerContext.setVariable(iteratorName, object);
	    if (!set.add(body.eval(innerContext)))
		return false;
	}
	return true;
    }

}
