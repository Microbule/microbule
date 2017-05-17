package org.microbule.osgi.container;

import org.osgi.framework.Bundle;

public class BundleDelegatingClassLoader extends ClassLoader {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Bundle bundle;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public BundleDelegatingClassLoader(ClassLoader parent, Bundle bundle) {
        super(parent);
        this.bundle = bundle;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return bundle == null ? null : bundle.loadClass(name);
    }
}
