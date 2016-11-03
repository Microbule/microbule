package org.microbule.api;

import java.util.Map;

public interface JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a JAX-RS proxy for the service at <code>baseAddress</code> which supports the
     * JAX-RS annotated <code>serviceInterface</code>.
     *
     * @param serviceInterface the service interface
     * @param baseAddress the base address of the service
     * @param properties the properties used to customize the proxy
     * @param <T> the proxy type
     * @return the proxy
     */
    <T> T createProxy(Class<T> serviceInterface, String baseAddress, Map<String,Object> properties);
}
