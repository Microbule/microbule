package org.microbule.util.reflect;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class TypesTest  extends MicrobuleTestCase {
    @Test
    public void testIsUtilsClass() throws Exception {
        assertIsUtilsClass(Types.class);
    }

    @Test
    public void testGetTypeParameter() throws Exception {
        final Type type = MyType.class.getDeclaredField("map").getGenericType();
        assertEquals(String.class, Types.getTypeParameter(type, Map.class, 0));
        assertEquals(Integer.class, Types.getTypeParameter(type, Map.class, 1));
    }

    public static class MyType {
        private final Map<String,Integer> map = new HashMap<>();
    }
}