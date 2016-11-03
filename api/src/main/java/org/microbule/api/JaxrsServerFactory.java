package org.microbule.api;

import java.util.Map;

public interface JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new JAX-RS server
     * @param serviceInterface the service interface
     * @param serviceImplementation the service implementation object
     * @param baseAddress the base address
     * @param properties the properties
     * @return the server
     */
    JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, String baseAddress, Map<String,Object> properties);
}
