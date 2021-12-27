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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import com.vgu.se.jocl.types.AnyType;
import com.vgu.se.jocl.types.BagType;
import com.vgu.se.jocl.types.Class;
import com.vgu.se.jocl.types.IntegerType;
import com.vgu.se.jocl.types.InvalidType;
import com.vgu.se.jocl.types.MessageType;
import com.vgu.se.jocl.types.OrderedSetType;
import com.vgu.se.jocl.types.RealType;
import com.vgu.se.jocl.types.SequenceType;
import com.vgu.se.jocl.types.SetType;
import com.vgu.se.jocl.types.StringType;
import com.vgu.se.jocl.types.TemplateParameterType;
import com.vgu.se.jocl.types.TupleType;
import com.vgu.se.jocl.types.UnlimitedNaturalType;
import com.vgu.se.jocl.types.VoidType;

public class ConformanceTest {

    @Test
    public void test_conforms_to_AnyType() {
        AnyType target = new AnyType();

        AnyType any = new AnyType();
        assertTrue(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertTrue(bag.conformsTo(target));

        Class clazz = new Class();
        assertTrue(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertTrue(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertTrue(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertTrue(orderedSet.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertTrue(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertTrue(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertTrue(set.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertTrue(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertTrue(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertTrue(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void AnyType_conforms_to_test() {
        AnyType target = new AnyType();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    public void test_conforms_to_BagType() {
        BagType target = new BagType(new Class());

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void BagType_conforms_to_test() {
        BagType target = new BagType(new Class());

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_collection_conforms_to_BagType() {
        BagType target = new BagType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertTrue(bagInteger.conformsTo(target));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(orderedSetInteger.conformsTo(target));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(sequenceInteger.conformsTo(target));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(setInteger.conformsTo(target));

        BagType bagClass = new BagType(new Class());
        assertFalse(bagClass.conformsTo(target));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(orderedSetClass.conformsTo(target));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(sequenceClass.conformsTo(target));

        SetType setClass = new SetType(new Class());
        assertFalse(setClass.conformsTo(target));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertTrue(bagUnlimitedNatural.conformsTo(target));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(orderedSetUnlimitedNatural.conformsTo(target));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(sequenceUnlimitedNatural.conformsTo(target));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(setUnlimitedNatural.conformsTo(target));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(bagReal.conformsTo(target));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(orderedSetReal.conformsTo(target));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(sequenceReal.conformsTo(target));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(setReal.conformsTo(target));
    }

    @Test
    public void BagType_conforms_to_test_collection() {
        BagType target = new BagType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertTrue(target.conformsTo(bagInteger));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(target.conformsTo(orderedSetInteger));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(target.conformsTo(sequenceInteger));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(target.conformsTo(setInteger));

        BagType bagClass = new BagType(new Class());
        assertFalse(target.conformsTo(bagClass));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSetClass));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequenceClass));

        SetType setClass = new SetType(new Class());
        assertFalse(target.conformsTo(setClass));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(bagUnlimitedNatural));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(orderedSetUnlimitedNatural));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(sequenceUnlimitedNatural));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(setUnlimitedNatural));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertTrue(target.conformsTo(bagReal));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(orderedSetReal));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(sequenceReal));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(setReal));
    }

    @Test
    public void test_conforms_to_OrderedSetType() {
        OrderedSetType target = new OrderedSetType(new Class());

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void OrderedSetType_conforms_to_test() {
        OrderedSetType target = new OrderedSetType(new Class());

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_collection_conforms_to_OrderedSetType() {
        OrderedSetType target = new OrderedSetType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(bagInteger.conformsTo(target));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertTrue(orderedSetInteger.conformsTo(target));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(sequenceInteger.conformsTo(target));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(setInteger.conformsTo(target));

        BagType bagClass = new BagType(new Class());
        assertFalse(bagClass.conformsTo(target));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(orderedSetClass.conformsTo(target));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(sequenceClass.conformsTo(target));

        SetType setClass = new SetType(new Class());
        assertFalse(setClass.conformsTo(target));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(bagUnlimitedNatural.conformsTo(target));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertTrue(orderedSetUnlimitedNatural.conformsTo(target));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(sequenceUnlimitedNatural.conformsTo(target));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(setUnlimitedNatural.conformsTo(target));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(bagReal.conformsTo(target));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(orderedSetReal.conformsTo(target));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(sequenceReal.conformsTo(target));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(setReal.conformsTo(target));
    }

    @Test
    public void OrderedSetType_conforms_to_test_collection() {
        OrderedSetType target = new OrderedSetType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(target.conformsTo(bagInteger));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertTrue(target.conformsTo(orderedSetInteger));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(target.conformsTo(sequenceInteger));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(target.conformsTo(setInteger));

        BagType bagClass = new BagType(new Class());
        assertFalse(target.conformsTo(bagClass));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSetClass));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequenceClass));

        SetType setClass = new SetType(new Class());
        assertFalse(target.conformsTo(setClass));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(bagUnlimitedNatural));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(orderedSetUnlimitedNatural));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(sequenceUnlimitedNatural));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(setUnlimitedNatural));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(bagReal));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertTrue(target.conformsTo(orderedSetReal));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(sequenceReal));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(setReal));
    }

    @Test
    public void test_conforms_to_SequenceType() {
        SequenceType target = new SequenceType(new Class());

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void SequenceType_conforms_to_test() {
        SequenceType target = new SequenceType(new Class());

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_collection_conforms_to_SequenceType() {
        SequenceType target = new SequenceType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(bagInteger.conformsTo(target));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(orderedSetInteger.conformsTo(target));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertTrue(sequenceInteger.conformsTo(target));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(setInteger.conformsTo(target));

        BagType bagClass = new BagType(new Class());
        assertFalse(bagClass.conformsTo(target));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(orderedSetClass.conformsTo(target));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(sequenceClass.conformsTo(target));

        SetType setClass = new SetType(new Class());
        assertFalse(setClass.conformsTo(target));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(bagUnlimitedNatural.conformsTo(target));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(orderedSetUnlimitedNatural.conformsTo(target));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertTrue(sequenceUnlimitedNatural.conformsTo(target));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(setUnlimitedNatural.conformsTo(target));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(bagReal.conformsTo(target));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(orderedSetReal.conformsTo(target));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(sequenceReal.conformsTo(target));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(setReal.conformsTo(target));
    }

    @Test
    public void SequenceType_conforms_to_test_collection() {
        SequenceType target = new SequenceType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(target.conformsTo(bagInteger));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(target.conformsTo(orderedSetInteger));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertTrue(target.conformsTo(sequenceInteger));

        SetType setInteger = new SetType(new IntegerType(1));
        assertFalse(target.conformsTo(setInteger));

        BagType bagClass = new BagType(new Class());
        assertFalse(target.conformsTo(bagClass));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSetClass));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequenceClass));

        SetType setClass = new SetType(new Class());
        assertFalse(target.conformsTo(setClass));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(bagUnlimitedNatural));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(orderedSetUnlimitedNatural));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(sequenceUnlimitedNatural));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(setUnlimitedNatural));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(bagReal));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(orderedSetReal));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertTrue(target.conformsTo(sequenceReal));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(setReal));
    }

    @Test
    public void test_conforms_to_SetType() {
        SetType target = new SetType(new Class());

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void SetType_conforms_to_test() {
        SetType target = new SetType(new Class());

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_collection_conforms_to_SetType() {
        SetType target = new SetType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(bagInteger.conformsTo(target));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(orderedSetInteger.conformsTo(target));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(sequenceInteger.conformsTo(target));

        SetType setInteger = new SetType(new IntegerType(1));
        assertTrue(setInteger.conformsTo(target));

        BagType bagClass = new BagType(new Class());
        assertFalse(bagClass.conformsTo(target));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(orderedSetClass.conformsTo(target));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(sequenceClass.conformsTo(target));

        SetType setClass = new SetType(new Class());
        assertFalse(setClass.conformsTo(target));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(bagUnlimitedNatural.conformsTo(target));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(orderedSetUnlimitedNatural.conformsTo(target));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(sequenceUnlimitedNatural.conformsTo(target));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertTrue(setUnlimitedNatural.conformsTo(target));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(bagReal.conformsTo(target));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(orderedSetReal.conformsTo(target));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(sequenceReal.conformsTo(target));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertFalse(setReal.conformsTo(target));
    }

    @Test
    public void SetType_conforms_to_test_collection() {
        SetType target = new SetType(new IntegerType(1));

        BagType bagInteger = new BagType(new IntegerType(1));
        assertFalse(target.conformsTo(bagInteger));

        OrderedSetType orderedSetInteger = new OrderedSetType(new IntegerType(1));
        assertFalse(target.conformsTo(orderedSetInteger));

        SequenceType sequenceInteger = new SequenceType(new IntegerType(1));
        assertFalse(target.conformsTo(sequenceInteger));

        SetType setInteger = new SetType(new IntegerType(1));
        assertTrue(target.conformsTo(setInteger));

        BagType bagClass = new BagType(new Class());
        assertFalse(target.conformsTo(bagClass));

        OrderedSetType orderedSetClass = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSetClass));

        SequenceType sequenceClass = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequenceClass));

        SetType setClass = new SetType(new Class());
        assertFalse(target.conformsTo(setClass));

        BagType bagUnlimitedNatural = new BagType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(bagUnlimitedNatural));

        OrderedSetType orderedSetUnlimitedNatural = new OrderedSetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(orderedSetUnlimitedNatural));

        SequenceType sequenceUnlimitedNatural = new SequenceType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(sequenceUnlimitedNatural));

        SetType setUnlimitedNatural = new SetType(new UnlimitedNaturalType(new BigInteger("1")));
        assertFalse(target.conformsTo(setUnlimitedNatural));

        BagType bagReal = new BagType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(bagReal));

        OrderedSetType orderedSetReal = new OrderedSetType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(orderedSetReal));

        SequenceType sequenceReal = new SequenceType(new RealType(new Double(1.0)));
        assertFalse(target.conformsTo(sequenceReal));

        SetType setReal = new SetType(new RealType(new Double(1.0)));
        assertTrue(target.conformsTo(setReal));
    }

    @Test
    public void test_conforms_to_Class() {
        Class target = new Class();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertTrue(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void Class_conforms_to_test() {
        Class target = new Class();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertTrue(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(new Double(1.0));
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_IntegerType() {
        IntegerType target = new IntegerType(new Integer(1));

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertTrue(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void IntegerType_conforms_to_test() {
        IntegerType target = new IntegerType(new Integer(1));

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertTrue(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(new Double(1.0));
        assertTrue(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType(new String("1.0"));
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_InvalidType() {
        InvalidType target = InvalidType.getSoleInstance();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(new Integer(1));
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(new Double(1.0));
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType(new String("1.0"));
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(voidType.conformsTo(target));
    }

    @Test
    public void InvalidType_conforms_to_test() {
        InvalidType target = InvalidType.getSoleInstance();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertTrue(target.conformsTo(bag));

        Class clazz = new Class();
        assertTrue(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(new Integer(1));
        assertTrue(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertTrue(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertTrue(target.conformsTo(orderedSet));

        RealType real = new RealType(new Double(1.0));
        assertTrue(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertTrue(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertTrue(target.conformsTo(set));

        StringType string = new StringType(new String("1.0"));
        assertTrue(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertTrue(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertTrue(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_MessageType() {
        MessageType target = new MessageType();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertTrue(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void MessageType_conforms_to_test() {
        MessageType target = new MessageType();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertTrue(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_RealType() {
        RealType target = new RealType(1.0);

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertTrue(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertTrue(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void RealType_conforms_to_test() {
        RealType target = new RealType(1.0);

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertTrue(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_StringType() {
        StringType target = new StringType("1");

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertTrue(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void StringType_conforms_to_test() {
        StringType target = new StringType("1");

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertTrue(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }
    
    @Test
    public void test_conforms_to_TemplateParameterType() {
        TemplateParameterType target = new TemplateParameterType();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertTrue(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void TemplateParameterType_conforms_to_test() {
        TemplateParameterType target = new TemplateParameterType();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertTrue(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_TupleType() {
        TupleType target = new TupleType();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertTrue(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void TupleType_conforms_to_test() {
        TupleType target = new TupleType();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertFalse(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertFalse(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertTrue(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }

    @Test
    public void test_conforms_to_UnlimitedNaturalType() {
        UnlimitedNaturalType target = new UnlimitedNaturalType(new BigInteger("1"));

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertTrue(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void UnlimitedNaturalType_conforms_to_test() {
        UnlimitedNaturalType target = new UnlimitedNaturalType(new BigInteger("1"));

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertFalse(target.conformsTo(bag));

        Class clazz = new Class();
        assertFalse(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertTrue(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertFalse(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertTrue(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertFalse(target.conformsTo(set));

        StringType string = new StringType("1");
        assertFalse(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertFalse(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertFalse(target.conformsTo(voidType));
    }


    @Test
    public void test_conforms_to_VoidType() {
        VoidType target = VoidType.getSoleInstance();

        AnyType any = new AnyType();
        assertFalse(any.conformsTo(target));

        BagType bag = new BagType(new Class());
        assertFalse(bag.conformsTo(target));

        Class clazz = new Class();
        assertFalse(clazz.conformsTo(target));

        IntegerType integer = new IntegerType(1);
        assertFalse(integer.conformsTo(target));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(invalid.conformsTo(target));

        MessageType message = new MessageType();
        assertFalse(message.conformsTo(target));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertFalse(orderedSet.conformsTo(target));

        RealType real = new RealType(1.0);
        assertFalse(real.conformsTo(target));

        SequenceType sequence = new SequenceType(new Class());
        assertFalse(sequence.conformsTo(target));

        SetType set = new SetType(new Class());
        assertFalse(set.conformsTo(target));

        StringType string = new StringType("1");
        assertFalse(string.conformsTo(target));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertFalse(templateParameter.conformsTo(target));

        TupleType tuple = new TupleType();
        assertFalse(tuple.conformsTo(target));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertFalse(unlimitedNatural.conformsTo(target));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(voidType.conformsTo(target));
    }

    @Test
    public void VoidType_conforms_to_test() {
        VoidType target = VoidType.getSoleInstance();

        AnyType any = new AnyType();
        assertTrue(target.conformsTo(any));

        BagType bag = new BagType(new Class());
        assertTrue(target.conformsTo(bag));

        Class clazz = new Class();
        assertTrue(target.conformsTo(clazz));

        IntegerType integer = new IntegerType(1);
        assertTrue(target.conformsTo(integer));

        InvalidType invalid = InvalidType.getSoleInstance();
        assertFalse(target.conformsTo(invalid));

        MessageType message = new MessageType();
        assertTrue(target.conformsTo(message));

        OrderedSetType orderedSet = new OrderedSetType(new Class());
        assertTrue(target.conformsTo(orderedSet));

        RealType real = new RealType(1.0);
        assertTrue(target.conformsTo(real));

        SequenceType sequence = new SequenceType(new Class());
        assertTrue(target.conformsTo(sequence));

        SetType set = new SetType(new Class());
        assertTrue(target.conformsTo(set));

        StringType string = new StringType("1");
        assertTrue(target.conformsTo(string));

        TemplateParameterType templateParameter = new TemplateParameterType();
        assertTrue(target.conformsTo(templateParameter));

        TupleType tuple = new TupleType();
        assertTrue(target.conformsTo(tuple));

        UnlimitedNaturalType unlimitedNatural = new UnlimitedNaturalType(new BigInteger("1"));
        assertTrue(target.conformsTo(unlimitedNatural));

        VoidType voidType = VoidType.getSoleInstance();
        assertTrue(target.conformsTo(voidType));
    }


    

}
