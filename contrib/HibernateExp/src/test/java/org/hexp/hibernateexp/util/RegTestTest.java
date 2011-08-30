/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hexp.hibernateexp.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegTestTest {

    public RegTestTest() {
    }

    @Test
    public void test1() {
        assertFalse(RegTest.isBackReference("This is not a back regerenceHello"));


    }

    @Test
    public void test2() {
        assertFalse(RegTest.isBackReference("This is also not a back reference Hello \\\\1 \\\\2"));
    }

    @Test
    public void test3() {
        assertTrue(RegTest.isBackReference("This is a back Reference \\1"));
    }

    @Test
    public void test4() {
        assertTrue(RegTest.isBackReference("This is also a back reference \\\\1 \\2"));
    }
}
