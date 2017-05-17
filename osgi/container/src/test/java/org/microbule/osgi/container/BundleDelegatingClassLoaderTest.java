package org.microbule.osgi.container;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.osgi.framework.Bundle;

import static org.mockito.Mockito.when;

public class BundleDelegatingClassLoaderTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private Bundle bundle;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testFindClass() throws Exception {
        BundleDelegatingClassLoader loader = new BundleDelegatingClassLoader(getClass().getClassLoader(), bundle);
        when(bundle.loadClass(MyClass.class.getName())).thenAnswer((Answer<Class<?>>) invocation -> MyClass.class);
        Class<?> clazz = loader.findClass(MyClass.class.getName());
        assertEquals(MyClass.class, clazz);
    }

    @Test
    public void testFindClassWithNullBundle() throws Exception {
        BundleDelegatingClassLoader loader = new BundleDelegatingClassLoader(getClass().getClassLoader(), null);
        assertNull(loader.findClass(MyClass.class.getName()));
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class MyClass {
    }
}