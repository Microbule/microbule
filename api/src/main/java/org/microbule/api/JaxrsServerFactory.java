package org.microbule.api;

import org.microbule.config.api.Config;

public interface JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    String ADDRESS_PROP = "serverAddress";

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new JAX-RS server
     *
     * @param serviceInterface      the service interface
     * @param serviceImplementation the service implementation object
     * @param config                the configuration
     * @return the server
     */
    JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, Config config);
}
