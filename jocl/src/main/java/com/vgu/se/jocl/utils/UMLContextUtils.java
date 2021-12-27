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

@author: ngpbh
***************************************************************************/

package com.vgu.se.jocl.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The Class UMLContextUtils contains various methods for querying over
 * UML context
 */
public class UMLContextUtils {

    /**
     * Validate UML Context.
     *
     * @param context the UML context
     * @return true, if valid
     */
    public static boolean validateContext(JSONArray context) {
        // TODO: Implement.
        return false;
    }

    /**
     * Checks if {@code className} is an actual class in this context.
     *
     * @param context   the UML context
     * @param className the class name
     * @return true, if this class is valid
     */
    public static boolean isClass(JSONArray context, String className) {
        if (context != null && className != null) {
            for (Object entity : context) {
                if (entity instanceof JSONObject) {
                    JSONObject obj = (JSONObject) entity;
                    if (className.equals(obj.get("class"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if {@code associationName} is an actual association in
     * this context.
     *
     * @param context         the UML context
     * @param associationName the association name
     * @return true, if the association is valid
     */
    public static boolean isAssociationClass(JSONArray context,
            String associationName) {
        if (context != null && associationName != null) {
            for (Object entity : context) {
                if (entity instanceof JSONObject) {
                    JSONObject jObject = (JSONObject) entity;
                    if (associationName
                            .equals(jObject.get("association"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if {@code propertyName} is an actual property of
     * {@code className} in this UML context.
     *
     * @param context      the UML context
     * @param className    the class name
     * @param propertyName the property name
     * @return true, if is property belongs to the class and the class
     *         also belongs to the UML context
     */
    public static boolean isPropertyOfClass(JSONArray context,
            String className, String propertyName) {
        if (context != null && className != null
                && propertyName != null) {
            for (Object entity : context) {
                if (entity instanceof JSONObject) {
                    JSONObject jObject = (JSONObject) entity;
                    if (className.equals(jObject.get("class"))) {
                        Object attributes = jObject.get("attributes");
                        if (attributes instanceof JSONArray) {
                            JSONArray jAtts = (JSONArray) attributes;
                            for (Object att : jAtts) {
                                if (att instanceof JSONObject) {
                                    JSONObject jAtt = (JSONObject) att;
                                    if (propertyName
                                            .equals(jAtt.get("name"))) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if {@code associationName} is an actual association of
     * {@code className} in this UML context.
     *
     * @param context         the UML context
     * @param className       the class name
     * @param associationName the association name
     * @return true, if is association is valid (and the class is in the
     *         context)
     */
    public static boolean isAssociationOfClass(JSONArray context,
            String className, String associationName) {
        if (context != null && className != null
                && associationName != null) {
            for (Object entity : context) {
                if (entity instanceof JSONObject) {
                    JSONObject jObject = (JSONObject) entity;
                    if (jObject.containsKey("association")) {
                        Object ends = jObject.get("ends");
                        Object classes = jObject.get("classes");
                        if (ends instanceof JSONArray
                                && classes instanceof JSONArray) {
                            JSONArray jEnds = (JSONArray) ends;
                            JSONArray jClasses = (JSONArray) classes;
                            if (className.equals(jClasses.get(0))
                                    && associationName
                                            .equals(jEnds.get(1))
                                    || className.equals(jClasses.get(1))
                                            && associationName.equals(
                                                    jEnds.get(0))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static String getAssociationOppositeClassName(
            JSONArray context, String assocName, String className) {
        for (Object object : context) {
            if (((JSONObject) object).containsKey("association")
                    && ((JSONObject) object).get("association")
                            .equals(assocName)) {
                JSONArray classes = (JSONArray) ((JSONObject) object)
                        .get("classes");
                if (classes.get(0).equals(className))
                    return (String) classes.get(1);
                else
                    return (String) classes.get(0);
            }
        }
        return null;
    }

    public static String getAttributeType(JSONArray plainUMLContext,
            String propertyClass, String propertyName) {
        for (Object entity : plainUMLContext) {
            if (((JSONObject) entity).containsKey("class")) {
                if (((JSONObject) entity).get("class")
                        .equals(propertyClass)) {
                    if (((JSONObject) entity)
                            .containsKey("attributes")) {
                        for (Object association : (JSONArray) ((JSONObject) entity)
                                .get("attributes")) {
                            if (((JSONObject) association).get("name")
                                    .equals(propertyName)) {
                                return (String) ((JSONObject) association)
                                        .get("type");
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isSuperClassOf(JSONArray plainUMLContext,
            String expectedSuperType, String targetType) {
        if (targetType == null)
            return true;
        if (expectedSuperType.equals(targetType))
            return true;
        while (true) {
            String superClazz = findDirectSuperClass(plainUMLContext,
                    targetType);
            if (superClazz == null)
                return false;
            if (expectedSuperType.equals(superClazz))
                return true;
            targetType = superClazz;
        }
    }

    private static String findDirectSuperClass(
            JSONArray plainUMLContext, String targetType) {
        for (Object entity : plainUMLContext) {
            JSONObject clazz = (JSONObject) entity;
            if (clazz.containsKey("class")
                    && targetType.equals(clazz.get("class"))
                    && clazz.containsKey("super")) {
                return (String) clazz.get("super");
            }
        }
        return null;
    }

    public static String getAssociation(JSONArray context,
            String className, String endName) {
        String association = null;
        for (Object object : context) {
            if (((JSONObject) object).containsKey("association")) {
                JSONArray classes = (JSONArray) ((JSONObject) object)
                        .get("classes");
                JSONArray ends = (JSONArray) ((JSONObject) object)
                        .get("ends");
                for (int index_end = 0; index_end < classes
                        .size(); index_end++) {
                    if (classes.get(index_end).equals(className)
                            && ends.get(index_end).equals(endName)) {
                        association = (String) ((JSONObject) object)
                                .get("association");
                        break;
                    }
                    ;
                }

            }
        }

        return association;
    }
    
    public static String getOppositeAssociationEnd(JSONArray context,
            String assocName, String endName) {
        String opposAssoc = null;

        for (int i = 0; i< context.size(); i++) {
            JSONObject o = (JSONObject) context.get(i);
            
            if (o.containsKey("association")
                    && o.get("association").equals(assocName)) {
                JSONArray ends = (JSONArray) o.get("ends");
                
                opposAssoc = ends.get(0).equals(endName) ?
                    (String) ends.get(1) :
                    (String) ends.get(0);
            }
        }
        return opposAssoc;
    }

    public static boolean isAssociationEndOfClass(JSONArray context,
            String className, String assoc) {

        for (int i = 0; i < context.size(); i++) {
            JSONObject o = (JSONObject) context.get(i);

            if (o.containsKey("association")) {
                JSONArray classes = (JSONArray) o.get("classes");
                JSONArray ends = (JSONArray) o.get("ends");

                if (classes.get(0).equals(className)
                        && ends.get(0).equals(assoc)) {
                    return true;
                }

                if (classes.get(1).equals(className)
                        && ends.get(1).equals(assoc)) {
                    return true;
                }
            }
        }
        return false;
    }
}
