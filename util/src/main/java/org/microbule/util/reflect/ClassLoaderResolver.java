package org.microbule.util.reflect;

public interface ClassLoaderResolver {
    ClassLoader resolveClassLoader(Class<?> targetType, Object caller);
}
