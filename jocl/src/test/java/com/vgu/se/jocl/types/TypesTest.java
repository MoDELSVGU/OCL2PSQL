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

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

public class TypesTest {

    @Test
    public void anyType() {
        AnyType anyType = new AnyType();
        assertEquals(anyType.TYPE.getType(), "Any");
    }

    @Test
    public void bagType() {
        BagType bagType = new BagType(new AnyType());
        assertEquals(bagType.TYPE.getType(), "Bag");
    }

    @Test
    public void classType() {
        Class clazz = new Class();
        assertEquals(clazz.TYPE.getType(), "Class");
    }

    @Test
    public void integerType() {
        IntegerType integerType = new IntegerType(new Integer(1));
        assertEquals(integerType.TYPE.getType(), "Integer");
    }

    @Test
    public void invalidType() {
        InvalidType invalidType = InvalidType.getSoleInstance();
        assertEquals(invalidType.TYPE.getType(), "Invalid");
    }

    @Test
    public void messageType() {
        MessageType messageType = new MessageType();
        assertEquals(messageType.TYPE.getType(), "Message");
    }

    @Test
    public void orderedSetType() {
        OrderedSetType orderedSetType = new OrderedSetType(new IntegerType(1));
        assertEquals(orderedSetType.TYPE.getType(), "OrderedSet");
    }

    @Test
    public void realType() {
        RealType realType = new RealType(new Double(1.0));
        assertEquals(realType.TYPE.getType(), "Real");
    }

    @Test
    public void sequenceType() {
        SequenceType sequenceType = new SequenceType(new IntegerType(1));
        assertEquals(sequenceType.TYPE.getType(), "Sequence");
    }

    @Test
    public void setType() {
        SetType setType = new SetType(new RealType(1.0));
        assertEquals(setType.TYPE.getType(), "Set");
    }

    @Test
    public void stringType() {
        StringType stringType = new StringType(new String("1.0"));
        assertEquals(stringType.TYPE.getType(), "String");
    }

    @Test
    public void templateParameterType() {
        TemplateParameterType templateParameterType = new TemplateParameterType();
        assertEquals(templateParameterType.TYPE.getType(), "TemplateParameter");
    }

    @Test
    public void tupleType() {
        TupleType tupleType = new TupleType();
        assertEquals(tupleType.TYPE.getType(), "Tuple");
    }

    @Test
    public void unlimitedNaturalType() {
        UnlimitedNaturalType unlimitedNaturalType = new UnlimitedNaturalType(new BigInteger("1"));
        assertEquals(unlimitedNaturalType.TYPE.getType(), "UnlimitedNatural");
    }

    @Test
    public void voidType() {
        VoidType voidType = VoidType.getSoleInstance();
        assertEquals(voidType.TYPE.getType(), "Void");
    }

}
