package org.microbule.config.core;

import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;

public class EmptyConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    public static final ConfigProvider INSTANCE = new EmptyConfigProvider();

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return EmptyConfig.INSTANCE;
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return EmptyConfig.INSTANCE;
    }
}
