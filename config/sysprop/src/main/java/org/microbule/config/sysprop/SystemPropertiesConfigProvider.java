package org.microbule.config.sysprop;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.core.PropertiesConfig;
import org.microbule.config.spi.ConfigProvider;

@Singleton
@Named("systemPropertiesConfigProvider")
public class SystemPropertiesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String NAME = "sysprop";

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

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return PRIORITY_SYSPROP;
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
