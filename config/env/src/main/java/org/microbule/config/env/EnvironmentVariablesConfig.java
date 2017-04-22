package org.microbule.config.env;

import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;

public class EnvironmentVariablesConfig implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return envConfig(serviceInterface, "proxy");
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return envConfig(serviceInterface, "server");
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Config envConfig(Class<?> serviceInterface, String subtype) {
        return new MapConfig(System.getenv(), "_")
                .group("microbule")
                .group(serviceInterface.getSimpleName())
                .group(subtype);
    }
}
