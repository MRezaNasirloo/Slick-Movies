package com.github.pedramrn.slick.parent.ui.home;

import com.github.pedramrn.slick.parent.util.IdBank;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-07-05
 */
public class IdBankTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testTag() throws Exception {
        Assert.assertEquals(0, IdBank.nextId("Foo"));
        Assert.assertEquals(1, IdBank.nextId("Foo"));
        IdBank.reset("Foo");
        Assert.assertEquals(0, IdBank.nextId("Foo"));
    }
}