package org.microbule.config.spi;

import org.microbule.config.api.Config;

public interface ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    int PRIORITY_SYSPROP = -200;

    int PRIORITY_ENV = -100;

    int DEFAULT_PRIORITY = 0;

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

    int priority();

    String name();
}
