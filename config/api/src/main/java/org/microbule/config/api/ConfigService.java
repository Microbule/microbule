package org.microbule.config.api;

public interface ConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Config getProxyConfig(Class<?> serviceInterface);

    Config getServerConfig(Class<?> serviceInterface);
}
