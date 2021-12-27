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

package com.vgu.se.jocl.types;

/**
 * The Class VoidType represents a null instance.
 */
public class VoidType extends Classifier {
    
    public final TypeEnum TYPE = TypeEnum.VOIDTYPE;

    private static VoidType NULL = null;

    /**
     * Instantiates a new void type.
     */
    private VoidType() {
    }

    /**
     * Gets the sole instance, OclVoid
     *
     * @return the sole instance
     */
    public static VoidType getSoleInstance() {
        if (NULL == null)
            NULL = new VoidType();
        return NULL;
    }

    /**
     * VoidType conforms to others except InvalidType.
     *
     * @param other an OCL-typed object
     * @return true, if it conforms to
     */
    @Override
    public boolean conformsTo(Classifier other) {
        if (this == other)
            return true;
        if (other == null)
            return true;
        if (other instanceof AnyType)
            return true;
        if (other instanceof InvalidType)
            return false;
        return true;
    }
}
