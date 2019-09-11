package org.vgu.sqlsi.ocl.context;

import org.vgu.sqlsi.ocl.exception.OclEvaluationException;

public interface OclContext {

    boolean isType(String typeName);

    boolean isEnum(String typeName);

    String getFullName(String typeName);

    Object getVariable(String name);

    Object getFieldValue(Object source, String propertyName) throws Exception;

    Object callMethod(Object source, String methodName,
	    Object... argumentValues) throws Exception;

    Object getType(String referredType) throws OclEvaluationException;
}
