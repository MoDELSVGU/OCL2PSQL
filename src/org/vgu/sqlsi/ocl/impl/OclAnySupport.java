package org.vgu.sqlsi.ocl.impl;

public class OclAnySupport {

    public static Boolean oclIsUndefined(Object a) {
	return a == null;
    }

    public static <T> T oclAsType(Object a, Class<T> clazz) {
	return clazz.cast(a);
    }

    public static Boolean oclIsKindOf(Object a, Class<?> clazz) {
	if (a == null)
	    return clazz == Void.class;
	else
	    return clazz.isInstance(a);
    }

    public static Boolean equals(Object a, Object b) {
	if (a == null)
	    return b == null;
	else
	    return a.equals(b);
    }

    public static Boolean notEquals(Object a, Object b) {
	if (a == null)
	    return b != null;
	else
	    return !a.equals(b);
    }
}
