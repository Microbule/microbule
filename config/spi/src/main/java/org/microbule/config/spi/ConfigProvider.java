package org.microbule.config.spi;

import org.microbule.config.api.Config;

public interface ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Config getServerConfig(Class<?> serviceInterface);

    Config getProxyConfig(Class<?> serviceInterface);
}
