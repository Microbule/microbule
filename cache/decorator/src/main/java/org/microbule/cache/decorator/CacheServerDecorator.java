package org.microbule.cache.decorator;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.microbule.cache.annotation.Cacheable;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("cacheServerDecorator")
public class CacheServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor object, Config config) {
        final Set<Method> methods = MethodUtils.getMethodsListWithAnnotation(object.serviceInterface(), Cacheable.class).stream().filter(method -> method.isAnnotationPresent(GET.class)).collect(Collectors.toSet());
        if (!methods.isEmpty()) {
            object.addProvider(new CacheDynamicFeature(object.serviceInterface(), methods));
        }
        object.addProvider(new CacheInfoProvider());
    }

    @Override
    public String name() {
        return "cache";
    }
}
