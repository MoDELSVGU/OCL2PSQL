/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
***************************************************************************/

package org.vgu.sqlsi.ocl.context;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vgu.sqlsi.ocl.exception.OclEvaluationException;
import org.vgu.sqlsi.ocl.exception.OclException;

public class DefaultOclContext implements OclContext {

    private final Map<String, Object> variables = new HashMap<String, Object>();

    public DefaultOclContext() {
    }

    public DefaultOclContext(Object self) {
	variables.put("self", self);
    }

    public DefaultOclContext(Map<String, Object> variables) {
	this.variables.putAll(variables);
    }

    @SuppressWarnings("unchecked")
    public Object callMethod(Object source, String operationName,
	    Object... argumentValues) throws Exception {
	try {
	    Class<?>[] argumentTypes = new Class<?>[argumentValues.length];
	    for (int i = 0; i < argumentValues.length; i++) {
		argumentTypes[i] = argumentValues[i].getClass();
	    }
	    if (source instanceof Class) {
		Method method = ((Class) source).getDeclaredMethod(
			operationName, argumentTypes);
		return method.invoke(source, argumentValues);
	    } else {
		Method method = source.getClass().getDeclaredMethod(
			operationName, argumentTypes);
		return method.invoke(source, argumentValues);
	    }
	} catch (NoSuchMethodException e) {
	    if ("allInstances".equals(operationName))
		throw new OclException(
			"allInstances() not supported with this context!");
	    else
		throw e;
	}
    }

    @SuppressWarnings("unchecked")
    public Object getFieldValue(Object source, String propertyName)
	    throws Exception {
	if ("type".equals(propertyName)) {
	    if (source == null) {
		return Void.class;
	    } else {
		return source.getClass();
	    }
	} else {
	    if (source == null) {
		// throw new
		// OclEvaluationException("OclUndefined has no property "
		// + propertyName);
		// Bugfix: as of OCL Specification chapter 11.2.3 each
		// property
		// accessed from OclUndefined results in OclUndefined
		// this means, that a call with source == null results in
		// null!
		return null;
	    }
	    if (source instanceof Collection) {
		List list = new ArrayList();
		for (Object o : (Collection) source) {
		    for (PropertyDescriptor pd : Introspector.getBeanInfo(
			    o.getClass()).getPropertyDescriptors()) {
			if (pd.getName().equals(propertyName))
			    list.add(pd.getReadMethod().invoke(o));
		    }
		}
		return list;
	    } else {
		if (source instanceof Class) {
		    Field field = ((Class) source)
			    .getDeclaredField(propertyName);
		    return field.get(((Class) source));

		} else {
		    for (PropertyDescriptor pd : Introspector.getBeanInfo(
			    source.getClass()).getPropertyDescriptors()) {
			if (pd.getName().equals(propertyName))
			    return pd.getReadMethod().invoke(source);
		    }
		    return source.getClass().getDeclaredField(propertyName)
			    .get(source);
		}
	    }
	}
    }

    public Object getVariable(String name) {
	return this.variables.get(name);
    }

    public void setVariable(String name, Object value) {
	this.variables.put(name, value);
    }

    public String getFullName(String typeName) {
	try {
	    Class<?> c = Class.forName(typeName.replaceAll("\\:\\:", "."));
	    return c.getName().replaceAll("\\.", "::");
	} catch (ClassNotFoundException e) {
	    for (Package p : Package.getPackages()) {
		try {
		    Class<?> c = Class.forName((p.getName() + "." + typeName)
			    .replaceAll("\\:\\:", "."));
		    return c.getName().replaceAll("\\.", "::");
		} catch (Throwable ee) {
		}
	    }
	    return typeName;
	}
    }

    public boolean isEnum(String typeName) {
	try {
	    Class<?> c = Class.forName(getFullName(typeName).replaceAll(
		    "\\:\\:", "."));
	    return c.isEnum();
	} catch (ClassNotFoundException e) {
	    return false;
	}
    }

    public boolean isType(String typeName) {
	try {
	    Class.forName(getFullName(typeName).replaceAll("\\:\\:", "."));
	    return true;
	} catch (ClassNotFoundException e) {
	    return false;
	}
    }

    public void setVariables(Map<String, Object> variables) {
	this.variables.putAll(variables);
    }

    public Object getType(String referredType) throws OclEvaluationException {
	String className = referredType.replaceAll("\\:\\:", "\\.");
	try {
	    return Class.forName(className);
	} catch (ClassNotFoundException e) {
	    throw new OclEvaluationException("Unknown Type: " + referredType);
	}
    }
}
