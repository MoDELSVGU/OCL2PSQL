package org.vgu.ocl2psql.ocl.impl;

public class OclStringSupport extends OclAnySupport {

    public static String add(String a, Object b) {
	return a + b;
    }
    
    public static String concat(String a, String b) {
	return a.concat(b);
    }
    
    public static Integer size(String a) {
	return a.length();
    }
    
    public static String toLower(String a) {
	return a.toLowerCase();
    }
    
    public static String toUpper(String a) {
	return a.toUpperCase();
    }
    
    public static String substring(String a, int begin, int end) {
	return a.substring(begin - 1, end);
    }

    public static String add(Number a, String b) {
	return a + b;
    }
    
}
