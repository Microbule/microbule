package org.microbule.util.reflect;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class DefaultClassLoaderResolverTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testResolveClassLoader() {
        final DefaultClassLoaderResolver resolver = new DefaultClassLoaderResolver();

        assertEquals(getClass().getClassLoader(), resolver.resolveClassLoader(DefaultClassLoaderResolverTest.class, this));
    }
}