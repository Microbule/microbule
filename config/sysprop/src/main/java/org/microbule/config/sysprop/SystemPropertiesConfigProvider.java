package org.microbule.config.sysprop;

import org.microbule.config.api.Config;
import org.microbule.config.core.PropertiesConfig;
import org.microbule.config.spi.ConfigProvider;

public class SystemPropertiesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return sysPropertiesConfig(serviceInterface, "proxy");
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return sysPropertiesConfig(serviceInterface, "server");
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Config sysPropertiesConfig(Class<?> serviceInterface, String subtype) {
        return new PropertiesConfig(System.getProperties())
                .group("microbule")
                .group(serviceInterface.getSimpleName())
                .group(subtype);
    }
}
