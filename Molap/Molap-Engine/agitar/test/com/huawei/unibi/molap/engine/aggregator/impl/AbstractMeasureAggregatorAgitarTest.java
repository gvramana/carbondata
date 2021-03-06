/**
 * Generated by Agitar build: AgitarOne Version 5.3.0.000022 (Build date: Jan 04, 2012) [5.3.0.000022]
 * JDK Version: 1.6.0_14
 *
 * Generated on 29 Jul, 2013 4:48:03 PM
 * Time to generate: 00:27.230 seconds
 *
 */

package com.huawei.unibi.molap.engine.aggregator.impl;

import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import com.huawei.unibi.molap.engine.datastorage.InMemoryCube;
import com.huawei.unibi.molap.engine.datastorage.Member;
import com.huawei.unibi.molap.engine.datastorage.MemberStore;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;

public class AbstractMeasureAggregatorAgitarTest extends AgitarTestCase {
    
    public Class getTargetClass()  {
        return AbstractMeasureAggregator.class;
    }
    
    public void testGetDimValueWithAggressiveMocks() throws Throwable {
        AbstractMeasureAggregator abstractMeasureAggregator = (AbstractMeasureAggregator) Mockingbird.getProxyObject(AbstractMeasureAggregator.class, true);
        KeyGenerator keyGenerator = (KeyGenerator) Mockingbird.getProxyObject(KeyGenerator.class);
        InMemoryCube inMemoryCube = (InMemoryCube) Mockingbird.getProxyObject(InMemoryCube.class);
        byte[] bytes = new byte[0];
        long[] longs = new long[1];
        MemberStore memberStore = (MemberStore) Mockingbird.getProxyObject(MemberStore.class);
        Member member = (Member) Mockingbird.getProxyObject(Member.class);
        setPrivateField(abstractMeasureAggregator, "generator", keyGenerator);
        setPrivateField(abstractMeasureAggregator, "slice", inMemoryCube);
        Mockingbird.enterRecordingMode();
        Mockingbird.setReturnValue(false, keyGenerator, "getKeyArray", "(byte[])long[]", longs, 1);
        longs[0] = 0L;
        Mockingbird.setReturnValue(inMemoryCube.getMemberCache(""), memberStore);
        Mockingbird.setReturnValue(memberStore.getMemberByID(0), member);
        Mockingbird.setReturnValue(member.toString(), "");
        Mockingbird.enterTestMode(AbstractMeasureAggregator.class);
        String result = abstractMeasureAggregator.getDimValue(bytes, 0, 0, "", 0);
        assertEquals("result", "", result);
    }
}

