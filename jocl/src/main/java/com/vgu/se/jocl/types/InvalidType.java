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
 * The Class InvalidType represents an invalid instance.
 */
public class InvalidType extends Classifier {
    public final TypeEnum TYPE = TypeEnum.INVALIDTYPE;

    private static InvalidType oclInvalid = null;

    private InvalidType() {
    }

    /**
     * Gets the sole instance, Invalid.
     *
     * @return the sole instance
     */
    public static InvalidType getSoleInstance() {
        if (oclInvalid == null)
            oclInvalid = new InvalidType();
        return oclInvalid;
    }
    
    /**
     * InvalidType conforms to others except VoidType.
     *
     * @param other an OCL-typed object
     * @return true, if it conforms to
     */
    @Override
    public boolean conformsTo(Classifier other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (other instanceof VoidType)
            return false;
        return true;
    }

}
