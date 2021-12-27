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
 * The class Class represents data classes in data context.
 */
public class Class extends Classifier {
    
    public final TypeEnum TYPE = TypeEnum.CLASS;

    /**
     * The Class object conforms to itself and object of its super type
     * and possibly its super class in context.
     *
     * @param other an OCL-typed object
     * @return true, if it conforms to
     */
    @Override
    public boolean conformsTo(Classifier other) {
        //TODO: Support super class in UML
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (other instanceof AnyType)
            return true;
        if (other.getClass().isAssignableFrom(this.getClass()))
            return true;
        return false;
    }

}
