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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * The Class OrderedSetType. OrderedSetType is a collection type that describes a set of elements where each distinct element occurs only once in the
set. The elements are ordered by their position in the sequence. 
 */
public class OrderedSetType extends CollectionType<Classifier> implements Set<Classifier> {
    
    /** The type. */
    public final TypeEnum TYPE = TypeEnum.ORDEREDSETTYPE;

    /**
     * Instantiates a new ordered set type.
     *
     * @param clazz the clazz
     */
    public OrderedSetType(Classifier clazz) {
        this.clazz = clazz;
    }
    
    /**
     * Adds the.
     *
     * @param e the e
     * @return true, if successful
     */
    @Override
    public boolean add(Classifier e) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Adds the all.
     *
     * @param c the c
     * @return true, if successful
     */
    @Override
    public boolean addAll(Collection<? extends Classifier> c) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Clear.
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    /**
     * Contains.
     *
     * @param o the o
     * @return true, if successful
     */
    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Contains all.
     *
     * @param c the c
     * @return true, if successful
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Iterator.
     *
     * @return the iterator
     */
    @Override
    public Iterator<Classifier> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Removes the.
     *
     * @param o the o
     * @return true, if successful
     */
    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Removes the all.
     *
     * @param c the c
     * @return true, if successful
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Retain all.
     *
     * @param c the c
     * @return true, if successful
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Size.
     *
     * @return the int
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * To array.
     *
     * @return the object[]
     */
    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * To array.
     *
     * @param <T> the generic type
     * @param a the a
     * @return the t[]
     */
    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Please revise the conformance rules of OrderedSet in OCL 2.4 section 8.2.1.
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
        if (other instanceof AnyType)
            return true;
        if (other instanceof OrderedSetType) {
            return this.clazz.conformsTo(((OrderedSetType) other).clazz);
        }
        return false;
    }

}
