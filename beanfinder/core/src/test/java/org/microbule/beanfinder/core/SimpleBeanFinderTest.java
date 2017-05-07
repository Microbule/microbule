package org.microbule.beanfinder.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class SimpleBeanFinderTest extends MockObjectTestCase {

    @Test
    public void testWithList() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();

        final List<Serializable> list = finder.beanList(Serializable.class);
        finder.addBean("Hello");
        assertEquals(0, list.size());
        finder.addBean("World");
        finder.initialize();
        assertEquals(2, list.size());
        finder.removeBean("World");
        assertEquals(1, list.size());

        finder.removeBean("Hello");
        assertEquals(0, list.size());

    }

    @Test
    public void testWithMap() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();

        final Map<String,Serializable> map = finder.beanMap(Serializable.class, String::valueOf);
        finder.addBean("Hello");
        finder.addBean("World");
        assertEquals(0, map.size());
        finder.initialize();
        assertEquals(2, map.size());
        assertEquals("Hello", map.get("Hello"));
        assertEquals("World", map.get("World"));

        finder.removeBean("World");

        assertEquals(1, map.size());
        assertNull(map.get("World"));
    }

    @Test
    public void testWithRef() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        final AtomicReference<String> ref = finder.beanReference(String.class, "DEFAULT");
        finder.addBean("Hello");
        finder.addBean("World");
        assertEquals("DEFAULT", ref.get());
        finder.initialize();
        assertEquals("Hello", ref.get());
        finder.removeBean("Hello");
        assertEquals("DEFAULT", ref.get());
    }

    @Test
    public void testWithSortedSet() {
        final SimpleBeanFinder finder = new SimpleBeanFinder();
        final SortedSet<String> set = finder.beanSortedSet(String.class);

        finder.addBean("1");
        finder.addBean("2");
        finder.initialize();
        assertEquals(Sets.newHashSet("1", "2"), set);


    }
}