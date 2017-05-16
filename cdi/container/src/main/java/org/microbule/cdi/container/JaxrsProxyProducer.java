package org.microbule.cdi.container;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.api.JaxrsProxyFactory;
import org.microbule.api.JaxrsProxyReference;
import org.microbule.util.reflect.Types;

@Named("jaxrsProxyProducer")
@Singleton
public class JaxrsProxyProducer {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Produces
    @SuppressWarnings("unchecked")
    public <T> JaxrsProxyReference<T> createProxy(InjectionPoint point, JaxrsProxyFactory proxyFactory) {
        final Class<T> serviceInterface = Types.getTypeParameter(point.getType(), JaxrsProxyReference.class, 0);
        return () -> proxyFactory.createProxy(serviceInterface);
    }
}
