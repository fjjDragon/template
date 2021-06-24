package com.fish;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @descript:
 * @author: fjjDragon
 * @create: 2021-06-24 16:37
 **/

public class UnitTest {
    @Before
    public void before() {
        System.out.println("before test...");

    }

    @Test
    public void a() {
        System.out.println("test...");

    }

    @After
    public void After() {
        System.out.println("after test...");

    }
}