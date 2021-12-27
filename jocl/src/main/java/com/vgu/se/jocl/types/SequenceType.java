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
import java.util.List;
import java.util.ListIterator;

/**
 * The Class SequenceType. SequenceType is a collection type that describes a list of elements where each element may occur multiple times in the
sequence. The elements are ordered by their position in the sequence.
 */
public class SequenceType extends CollectionType<Classifier> implements List<Classifier> {
    
    /** The type. */
    public final TypeEnum TYPE = TypeEnum.SEQUENCETYPE;
    
    /**
     * Instantiates a new sequence type.
     *
     * @param clazz the clazz
     */
    public SequenceType(Classifier clazz) {
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
     * Adds the.
     *
     * @param index the index
     * @param element the element
     */
    @Override
    public void add(int index, Classifier element) {
        // TODO Auto-generated method stub

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
     * Adds the all.
     *
     * @param index the index
     * @param c the c
     * @return true, if successful
     */
    @Override
    public boolean addAll(int index, Collection<? extends Classifier> c) {
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
     * Gets the.
     *
     * @param index the index
     * @return the classifier
     */
    @Override
    public Classifier get(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Index of.
     *
     * @param o the o
     * @return the int
     */
    @Override
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
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
     * Last index of.
     *
     * @param o the o
     * @return the int
     */
    @Override
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * List iterator.
     *
     * @return the list iterator
     */
    @Override
    public ListIterator<Classifier> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * List iterator.
     *
     * @param index the index
     * @return the list iterator
     */
    @Override
    public ListIterator<Classifier> listIterator(int index) {
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
     * Removes the.
     *
     * @param index the index
     * @return the classifier
     */
    @Override
    public Classifier remove(int index) {
        // TODO Auto-generated method stub
        return null;
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
     * Sets the.
     *
     * @param index the index
     * @param element the element
     * @return the classifier
     */
    @Override
    public Classifier set(int index, Classifier element) {
        // TODO Auto-generated method stub
        return null;
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
     * Sub list.
     *
     * @param fromIndex the from index
     * @param toIndex the to index
     * @return the list
     */
    @Override
    public List<Classifier> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
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
     * Please revise the conformance rules of SequenceType in OCL 2.4 section 8.2.1.
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
        if (other instanceof SequenceType) {
            return this.clazz.conformsTo(((SequenceType) other).clazz);
        }
        return false;
    }

}
