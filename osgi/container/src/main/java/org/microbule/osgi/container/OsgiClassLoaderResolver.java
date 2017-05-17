package org.microbule.osgi.container;

import org.microbule.util.reflect.ClassLoaderResolver;
import org.osgi.framework.FrameworkUtil;

public class OsgiClassLoaderResolver implements ClassLoaderResolver {
//----------------------------------------------------------------------------------------------------------------------
// ClassLoaderResolver Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ClassLoader resolveClassLoader(Class<?> targetType, Object caller) {
        return new BundleDelegatingClassLoader(caller.getClass().getClassLoader(), FrameworkUtil.getBundle(targetType));
    }
}
