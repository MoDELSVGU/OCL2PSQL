package org.vgu.ocl2psql.ocl.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.vgu.ocl2psql.ocl.exception.OclEvaluationException;

public class OclCollectionSupport {

    public static Object first(List<?> source) {
	return source.get(0);
    }

    public static Object last(List<?> source) {
	return source.get(source.size() - 1);
    }

    public static Object first(SortedSet<?> source) {
	return source.first();
    }

    public static Object last(SortedSet<?> source) {
	return source.last();
    }

    @SuppressWarnings("unchecked")
    public static List<?> asSequence(Collection<?> source) {
	return new ArrayList(source);
    }

    @SuppressWarnings("unchecked")
    public static TreeSet<?> asOrderedSet(Collection<?> source) {
	return new TreeSet(source);
    }

    @SuppressWarnings("unchecked")
    public static Set<?> asSet(Collection<?> source) {
	return new HashSet(source);
    }

    public static Collection<?> asBag(Collection<?> source) {
	return source;
    }

    public static Boolean isEmpty(Collection<?> source) {
	return source.isEmpty();
    }

    public static Boolean notEmpty(Collection<?> source) {
	return !source.isEmpty();
    }

    public static Integer size(Collection<?> source) {
	return source.size();
    }

    public static Boolean includes(Collection<?> source, Object target) {
	return source.contains(target);
    }

    public static Boolean excludes(Collection<?> source, Object target) {
	return !source.contains(target);
    }

    public static Integer count(Collection<?> source, Object target) {
	int count = 0;
	for (Object object : source) {
	    if (object == target)
		count++;
	}
	return count;
    }

    public static Boolean includesAll(Collection<?> source,
	    Collection<?> targets) {
	return source.containsAll(targets);
    }

    public static Boolean excludesAll(Collection<?> source,
	    Collection<?> targets) {
	for (Object object : targets) {
	    if (source.contains(object))
		return false;
	}
	return true;
    }

    public static Double sum(Collection<?> source)
	    throws OclEvaluationException {
	double sum = 0;
	for (Object object : source) {
	    if (object instanceof Number) {
		sum += ((Number) object).doubleValue();
	    } else {
		throw new OclEvaluationException(
			"cannot make a sum of a collection of "
				+ object.getClass().getName());
	    }
	}
	return sum;
    }

    public static Object at(List<?> collection, Number index) {
	return collection.get(index.intValue() - 1);
    }

    public static Integer indexOf(List<?> collection, Object body) {
	return collection.indexOf(body) + 1;
    }

    @SuppressWarnings("unchecked")
    public static Collection union(Collection collection,
	    Collection bodyCollection) {
	if (collection instanceof Set) {
	    Set set = new HashSet(collection);
	    set.addAll(bodyCollection);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet set = new TreeSet(((SortedSet) collection).comparator());
	    set.addAll(collection);
	    set.addAll(bodyCollection);
	    return set;
	} else {
	    List list = new ArrayList(collection);
	    list.addAll(bodyCollection);
	    return list;
	}
    }

    public static Boolean equals(Collection<?> source, Collection<?> target) {
	if (source == target)
	    return true;
	if (!source.containsAll(target))
	    return false;
	if (!target.containsAll(source))
	    return false;
	return true;
    }

    @SuppressWarnings("unchecked")
    public static Collection subtract(Collection source, Collection target) {
	if (source instanceof Set) {
	    Set set = new HashSet();
	    for (Object a : source) {
		if (!target.contains(a)) {
		    set.add(a);
		}
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    TreeSet set = new TreeSet(((SortedSet) source).comparator());
	    for (Object a : source) {
		if (!target.contains(a)) {
		    set.add(a);
		}
	    }
	    return set;
	} else {
	    List list = new ArrayList();
	    for (Object a : source) {
		if (!target.contains(a)) {
		    list.add(a);
		}
	    }
	    return list;
	}
    }

    @SuppressWarnings("unchecked")
    public static Collection including(Collection collection, Object body) {
	if (collection.contains(body)) {
	    return collection;
	}
	if (collection instanceof Set) {
	    Set set = new HashSet(collection);
	    set.add(body);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet set = new TreeSet(((SortedSet) collection).comparator());
	    set.addAll(collection);
	    set.add(body);
	    return set;
	} else {
	    List list = new ArrayList(collection);
	    list.add(body);
	    return list;
	}
    }

    @SuppressWarnings("unchecked")
    public static Collection excluding(Collection collection, Object body) {
	if (collection instanceof Set) {
	    Set set = new HashSet(collection);
	    set.remove(body);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet set = new TreeSet(((SortedSet) collection).comparator());
	    set.addAll(collection);
	    set.remove(body);
	    return set;
	} else {
	    List list = new ArrayList(collection);
	    list.remove(body);
	    return list;
	}
    }
}
