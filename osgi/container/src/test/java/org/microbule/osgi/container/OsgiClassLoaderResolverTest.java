package org.microbule.osgi.container;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;
import org.osgi.framework.Bundle;

public class OsgiClassLoaderResolverTest extends MockObjectTestCase {

    @Mock
    private Bundle bundle;

    @Test
    public void testResolveClassLoader() {
        final OsgiClassLoaderResolver resolver = new OsgiClassLoaderResolver();
        final ClassLoader loader = resolver.resolveClassLoader(MyClass.class, this);
        assertTrue(loader instanceof BundleDelegatingClassLoader);
    }

    public static class MyClass {

    }
}