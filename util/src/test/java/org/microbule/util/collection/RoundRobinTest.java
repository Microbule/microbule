package org.microbule.util.collection;

import java.util.LinkedList;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class RoundRobinTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testWithEmptyList() {
        RoundRobin<String> balancer = new RoundRobin<>(new LinkedList<>());
        assertNull(balancer.nextItem());
    }

    @Test
    public void testWithNullList() {
        RoundRobin<String> balancer = new RoundRobin<>(new LinkedList<>());
        assertNull(balancer.nextItem());
    }

    @Test
    public void testWithOneItem() {
        RoundRobin<String> balancer = new RoundRobin<>(Lists.newArrayList("one"));
        assertEquals("one", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
    }

    @Test
    public void testRoundRobin() {
        RoundRobin<String> balancer = new RoundRobin<>(Lists.newArrayList("one", "two", "three"));
        assertEquals("one", balancer.nextItem());
        assertEquals("two", balancer.nextItem());
        assertEquals("three", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
    }

    @Test
    public void testRoundRobinWithDups() {
        RoundRobin<String> balancer = new RoundRobin<>(Lists.newArrayList("one", "one", "two", "three"));
        assertEquals("one", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
        assertEquals("two", balancer.nextItem());
        assertEquals("three", balancer.nextItem());
        assertEquals("one", balancer.nextItem());
    }
}