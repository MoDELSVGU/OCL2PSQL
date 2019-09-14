package org.vgu.ocl2psql.ocl.impl;

public class OclBooleanSupport extends OclAnySupport {

    public static Boolean or(Boolean a, Boolean b) {
	return a || b;
    }

    public static Boolean and(Boolean a, Boolean b) {
	return a && b;
    }

    public static Boolean xor(Boolean a, Boolean b) {
	return a ^ b;
    }

    public static Boolean not(Boolean a) {
	return !a;
    }

    public static Boolean equals(Boolean a, Boolean b) {
	return a.booleanValue() == b.booleanValue();
    }

    public static Boolean notEquals(Boolean a, Boolean b) {
	return a.booleanValue() != b.booleanValue();
    }

    public static Boolean implies(Boolean a, Boolean b) {
	return a ? a & b : true;
    }
}
