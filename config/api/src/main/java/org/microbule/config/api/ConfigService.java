package org.microbule.config.api;

public interface ConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Config getProxyConfig(Class<?> serviceInterface);

    Config getServerConfig(Class<?> serviceInterface);

    Config getProxyConfig(Class<?> serviceInterface, Config overrides);

    Config getServerConfig(Class<?> serviceInterface, Config overrides);
}
