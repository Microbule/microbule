package org.microbule.decorator.cache;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.microbule.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CacheDynamicFeature implements DynamicFeature {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDynamicFeature.class);

    private final Class<?> serviceInterface;
    private final Set<Method> methods;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CacheDynamicFeature(Class<?> serviceInterface, Set<Method> methods) {
        this.serviceInterface = serviceInterface;
        this.methods = methods;
    }

//----------------------------------------------------------------------------------------------------------------------
// DynamicFeature Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        try {
            final Method resourceMethod = serviceInterface.getMethod(resourceInfo.getResourceMethod().getName(), resourceInfo.getResourceMethod().getParameterTypes());
            if (methods.contains(resourceMethod)) {
                LOGGER.info("Adding cache filter to method {}({})...", resourceMethod.getName(), Arrays.stream(resourceMethod.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
                context.register(new ContainerCacheFilter(resourceMethod.getAnnotation(Cacheable.class)));
            }
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Method {}() is not found on service interface {}.", resourceInfo.getResourceMethod().getName(), serviceInterface.getCanonicalName());
        }
    }
}
