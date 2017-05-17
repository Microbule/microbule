package org.microbule.util.reflect;

public class DefaultClassLoaderResolver implements ClassLoaderResolver {
//----------------------------------------------------------------------------------------------------------------------
// ClassLoaderResolver Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ClassLoader resolveClassLoader(Class<?> targetType, Object caller) {
        return targetType.getClassLoader();
    }
}
