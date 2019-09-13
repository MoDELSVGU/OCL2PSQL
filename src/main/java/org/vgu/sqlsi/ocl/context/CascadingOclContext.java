package org.vgu.sqlsi.ocl.context;

import java.util.HashMap;
import java.util.Map;

import org.vgu.sqlsi.ocl.exception.OclEvaluationException;

public class CascadingOclContext implements OclContext {

    private final OclContext delegate;
    /**
     * @param typeName
     * @return
     * @see org.vgu.securesql.ocl.context.context.OclContext#getFullName(java.lang.String)
     */
    public String getFullName(String typeName) {
	return delegate.getFullName(typeName);
    }

    /**
     * @param typeName
     * @return
     * @see org.vgu.securesql.ocl.context.context.OclContext#isEnum(java.lang.String)
     */
    public boolean isEnum(String typeName) {
	return delegate.isEnum(typeName);
    }

    /**
     * @param typeName
     * @return
     * @see org.vgu.securesql.ocl.context.context.OclContext#isType(java.lang.String)
     */
    public boolean isType(String typeName) {
	return delegate.isType(typeName);
    }

    private final Map<String, Object> variables = new HashMap<String, Object>();

    public CascadingOclContext(OclContext delegate) {
	this.delegate = delegate;
    }

    public CascadingOclContext(OclContext delegate, Map<String, Object> variables) {
	this(delegate);
	this.variables.putAll(variables);
    }



    public Object callMethod(Object source, String methodName,
	    Object... argumentValues) throws Exception {
	return this.delegate.callMethod(source, methodName, argumentValues);
    }

    public Object getFieldValue(Object source, String propertyName)
	    throws Exception {
	return this.delegate.getFieldValue(source, propertyName);
    }

    public Object getVariable(String name) {
	if (this.variables.containsKey(name))
	    return this.variables.get(name);
	return this.delegate.getVariable(name);
    }

    public void setVariable(String name, Object value) {
	this.variables.put(name, value);
    }

    public Object getType(String referredType) throws OclEvaluationException {
        return this.delegate.getType(referredType);
    }
}
