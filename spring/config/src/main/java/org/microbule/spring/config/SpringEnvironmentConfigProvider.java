package org.microbule.spring.config;

import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringEnvironmentConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Autowired
    private Environment environment;

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return new SpringEnvironmentConfig(environment).group(serviceInterface.getSimpleName()).group("proxy");
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return new SpringEnvironmentConfig(environment).group(serviceInterface.getSimpleName()).group("server");
    }

    @Override
    public String name() {
        return "spring";
    }
}
