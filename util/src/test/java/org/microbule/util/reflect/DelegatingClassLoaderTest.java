package org.microbule.util.reflect;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class DelegatingClassLoaderTest extends MockObjectTestCase {
    @Test
    public void testConstructor() {
        final URLClassLoader delegate = new URLClassLoader(new URL[]{DelegatingClassLoader.class.getProtectionDomain().getCodeSource().getLocation()});
        final DelegatingClassLoader loader = new DelegatingClassLoader(ClassLoader.getSystemClassLoader(), delegate);
        assertEquals(delegate, loader.getDelegate());
        assertEquals(ClassLoader.getSystemClassLoader(), loader.getParent());
    }

    @Test
    public void testEquals() {
        final URLClassLoader delegate = new URLClassLoader(new URL[]{DelegatingClassLoader.class.getProtectionDomain().getCodeSource().getLocation()});
        final DelegatingClassLoader loader1 = new DelegatingClassLoader(ClassLoader.getSystemClassLoader(), delegate);
        final DelegatingClassLoader loader2 = new DelegatingClassLoader(ClassLoader.getSystemClassLoader(), delegate);
        assertNotEquals(loader1, null);
        assertNotEquals(loader1, "FOO");
        assertEquals(loader1, loader1);
        assertEquals(loader1, loader2);
    }

    @Test
    public void testHashCode() {
        final URLClassLoader delegate = new URLClassLoader(new URL[]{DelegatingClassLoader.class.getProtectionDomain().getCodeSource().getLocation()});
        final DelegatingClassLoader loader1 = new DelegatingClassLoader(ClassLoader.getSystemClassLoader(), delegate);
        final DelegatingClassLoader loader2 = new DelegatingClassLoader(ClassLoader.getSystemClassLoader(), delegate);
        assertEquals(loader1.hashCode(), loader2.hashCode());
    }

    @Test
    public void testLoadClass() throws Exception {
        final URLClassLoader delegate = new URLClassLoader(new URL[]{DelegatingClassLoader.class.getProtectionDomain().getCodeSource().getLocation()});
        final DelegatingClassLoader loader = new DelegatingClassLoader(new BlacklistedClassLoader("org.microbule"), delegate);
        assertEquals(delegate.loadClass(DelegatingClassLoader.class.getName()), loader.findClass(DelegatingClassLoader.class.getName()));
    }

    private static class BlacklistedClassLoader extends ClassLoader {
        private final String blacklist;

        public BlacklistedClassLoader(String blacklist) {
            this.blacklist = blacklist;
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            if(name.startsWith(blacklist)) {
                throw new ClassNotFoundException("Not found!");
            }
            return super.loadClass(name);
        }
    }
}