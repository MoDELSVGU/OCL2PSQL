package org.vgu.ocl2psql.ocl.impl;

public class OclNumberSupport extends OclAnySupport {

    public static Boolean equals(Number a, Number b) {
	return a.doubleValue() == b.doubleValue();
    }
    
    public static Boolean notEquals(Number a, Number b) {
	return a.doubleValue() != b.doubleValue();
    }
    
    public static Boolean lessThan(Number a, Number b) {
	return a.doubleValue() < b.doubleValue();
    }
    
    public static Boolean greaterThan(Number a, Number b) {
	return a.doubleValue() > b.doubleValue();
    }
    
    public static Boolean atLeast(Number a, Number b) {
	return a.doubleValue() >= b.doubleValue();
    }
    
    public static Boolean atMost(Number a, Number b) {
	return a.doubleValue() <= b.doubleValue();
    }
    
    public static Number add(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return a.doubleValue() + b.doubleValue();
	} else if (a instanceof Float ^ b instanceof Float) {
	    return a.floatValue() + b.floatValue();
	} else if (a instanceof Long ^ b instanceof Long) {
	    return a.longValue() + b.longValue();
	} else {
	    return a.intValue() + b.intValue();
	}
    }
    
    public static Number subtract(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return a.doubleValue() - b.doubleValue();
	} else if (a instanceof Float ^ b instanceof Float) {
	    return a.floatValue() - b.floatValue();
	} else if (a instanceof Long ^ b instanceof Long) {
	    return a.longValue() - b.longValue();
	} else {
	    return a.intValue() - b.intValue();
	}
    }
    
    public static Number multiply(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return a.doubleValue() * b.doubleValue();
	} else if (a instanceof Float ^ b instanceof Float) {
	    return a.floatValue() * b.floatValue();
	} else if (a instanceof Long ^ b instanceof Long) {
	    return a.longValue() * b.longValue();
	} else {
	    return a.intValue() * b.intValue();
	}
    }
    
    public static Number divide(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return a.doubleValue() / b.doubleValue();
	} else if (a instanceof Float ^ b instanceof Float) {
	    return a.floatValue() / b.floatValue();
	} else if (a instanceof Long ^ b instanceof Long) {
	    return a.longValue() / b.longValue();
	} else {
	    return a.intValue() / b.intValue();
	}
    }
    
    public static Number mod(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return a.doubleValue() % b.doubleValue();
	} else if (a instanceof Float ^ b instanceof Float) {
	    return a.floatValue() % b.floatValue();
	} else if (a instanceof Long ^ b instanceof Long) {
	    return a.longValue() % b.longValue();
	} else {
	    return a.intValue() % b.intValue();
	}
    }
    
    public static Number div(Number a, Number b) {
	return a.intValue() / b.intValue();
    }
    
    public static Number abs(Number a) {
	if (a instanceof Double) {
	    return Math.abs(a.doubleValue());
	} else if (a instanceof Float) {
	    return Math.abs(a.floatValue());
	} else if (a instanceof Long) {
	    return Math.abs(a.longValue());
	} else {
	    return Math.abs(a.intValue());
	}
    }
    
    public static Number max(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return Math.max(a.doubleValue(),b.doubleValue());
	} else if (a instanceof Float ^ b instanceof Float) {
	    return Math.max(a.floatValue(), b.floatValue());
	} else if (a instanceof Long ^ b instanceof Long) {
	    return Math.max(a.longValue(), b.longValue());
	} else {
	    return Math.max(a.intValue(), b.intValue());
	}
    }
    
    public static Number min(Number a, Number b) {
	if (a instanceof Double || b instanceof Double) {
	    return Math.min(a.doubleValue(),b.doubleValue());
	} else if (a instanceof Float ^ b instanceof Float) {
	    return Math.min(a.floatValue(), b.floatValue());
	} else if (a instanceof Long ^ b instanceof Long) {
	    return Math.min(a.longValue(), b.longValue());
	} else {
	    return Math.min(a.intValue(), b.intValue());
	}
    }
    
    public static Number round(Number a) {
	if (a instanceof Double) {
	    return Math.round(a.doubleValue());
	} else {
	    return Math.round(a.floatValue());
	}
    }
    
    public static Number floor(Number a) {
	if (a instanceof Double) {
	    return Math.floor(a.doubleValue());
	} else {
	    return Math.floor(a.floatValue());
	}
    }
    
}
