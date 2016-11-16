package org.microbule.decorator.cache;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.GET;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.microbule.annotation.Cacheable;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class CacheServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig object) {
        final Set<Method> methods = MethodUtils.getMethodsListWithAnnotation(object.getServiceInterface(), Cacheable.class).stream().filter(method -> method.isAnnotationPresent(GET.class)).collect(Collectors.toSet());
        if (!methods.isEmpty()) {
            object.addProvider(new CacheDynamicFeature(object.getServiceInterface(), methods));
        }
        object.addProvider(new CacheInfoProvider());
    }
}
