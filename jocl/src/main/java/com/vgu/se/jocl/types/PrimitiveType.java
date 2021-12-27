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

// TODO: Auto-generated Javadoc
/**
 * The Class PrimitiveType handles single literal value instance.
 *
 * @param <E> the element type
 */
public abstract class PrimitiveType<E> extends DataType {

    /** The value. */
    private E value;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Instantiates a new primitive type.
     *
     * @param e the value
     */
    public PrimitiveType (E e) {
        this.setValue(e);
    }
}
