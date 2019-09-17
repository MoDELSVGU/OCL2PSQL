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

    public static Object first(List<Object> source) {
	return source.get(0);
    }

    public static Object last(List<Object> source) {
	return source.get(source.size() - 1);
    }

    public static Object first(SortedSet<Object> source) {
	return source.first();
    }

    public static Object last(SortedSet<Object> source) {
	return source.last();
    }

    public static List<Object> asSequence(Collection<Object> source) {
	return new ArrayList<>(source);
    }

    public static TreeSet<Object> asOrderedSet(Collection<Object> source) {
	return new TreeSet<>(source);
    }

    public static Set<Object> asSet(Collection<Object> source) {
	return new HashSet<>(source);
    }

    public static Collection<Object> asBag(Collection<Object> source) {
	return source;
    }

    public static Boolean isEmpty(Collection<Object> source) {
	return source.isEmpty();
    }

    public static Boolean notEmpty(Collection<Object> source) {
	return !source.isEmpty();
    }

    public static Integer size(Collection<Object> source) {
	return source.size();
    }

    public static Boolean includes(Collection<Object> source, Object target) {
	return source.contains(target);
    }

    public static Boolean excludes(Collection<Object> source, Object target) {
	return !source.contains(target);
    }

    public static Integer count(Collection<Object> source, Object target) {
	int count = 0;
	for (Object object : source) {
	    if (object == target)
		count++;
	}
	return count;
    }

    public static Boolean includesAll(Collection<Object> source,
	    Collection<Object> targets) {
	return source.containsAll(targets);
    }

    public static Boolean excludesAll(Collection<Object> source,
	    Collection<Object> targets) {
	for (Object object : targets) {
	    if (source.contains(object))
		return false;
	}
	return true;
    }

    public static Double sum(Collection<Object> source)
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

    public static Object at(List<Object> collection, Number index) {
	return collection.get(index.intValue() - 1);
    }

    public static Integer indexOf(List<Object> collection, Object body) {
	return collection.indexOf(body) + 1;
    }

    public static Collection<Object> union(Collection<Object> collection,
	    Collection<Object> bodyCollection) {
	if (collection instanceof Set) {
	    Set<Object> set = new HashSet<>(collection);
	    set.addAll(bodyCollection);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet<Object> set = new TreeSet<Object>(((SortedSet<Object>) collection).comparator());
	    set.addAll(collection);
	    set.addAll(bodyCollection);
	    return set;
	} else {
	    List<Object> list = new ArrayList<>(collection);
	    list.addAll(bodyCollection);
	    return list;
	}
    }

    public static Boolean equals(Collection<Object> source, Collection<Object> target) {
	if (source == target)
	    return true;
	if (!source.containsAll(target))
	    return false;
	if (!target.containsAll(source))
	    return false;
	return true;
    }

    public static Collection<Object> subtract(Collection<Object> source, Collection<Object> target) {
	if (source instanceof Set) {
	    Set<Object> set = new HashSet<>();
	    for (Object a : source) {
		if (!target.contains(a)) {
		    set.add(a);
		}
	    }
	    return set;
	} else if (source instanceof SortedSet) {
	    TreeSet<Object> set = new TreeSet<>(((SortedSet<Object>) source).comparator());
	    for (Object a : source) {
		if (!target.contains(a)) {
		    set.add(a);
		}
	    }
	    return set;
	} else {
	    List<Object> list = new ArrayList<Object>();
	    for (Object a : source) {
		if (!target.contains(a)) {
		    list.add(a);
		}
	    }
	    return list;
	}
    }

    public static Collection<Object> including(Collection<Object> collection, Object body) {
	if (collection.contains(body)) {
	    return collection;
	}
	if (collection instanceof Set) {
	    Set<Object> set = new HashSet<Object>(collection);
	    set.add(body);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet<Object> set = new TreeSet<Object>(((SortedSet<Object>) collection).comparator());
	    set.addAll(collection);
	    set.add(body);
	    return set;
	} else {
	    List<Object> list = new ArrayList<Object>(collection);
	    list.add(body);
	    return list;
	}
    }

    public static Collection<Object> excluding(Collection<Object> collection, Object body) {
	if (collection instanceof Set) {
	    Set<Object> set = new HashSet<Object>(collection);
	    set.remove(body);
	    return set;
	} else if (collection instanceof SortedSet) {
	    TreeSet<Object> set = new TreeSet<Object>(((SortedSet<Object>) collection).comparator());
	    set.addAll(collection);
	    set.remove(body);
	    return set;
	} else {
	    List<Object> list = new ArrayList<Object>(collection);
	    list.remove(body);
	    return list;
	}
    }
}
