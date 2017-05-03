package org.microbule.config.spi;

import org.microbule.config.api.Config;

public interface ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves the server configuration for a specified service interface.
     *
     * @param serviceInterface the service interface
     * @return the server configuration
     */
    Config getServerConfig(Class<?> serviceInterface);

    /**
     * Retrieves the proxy configuration for a specified service interface.
     *
     * @param serviceInterface the service interface
     * @return the proxy configuration
     */
    Config getProxyConfig(Class<?> serviceInterface);

    String name();
}
