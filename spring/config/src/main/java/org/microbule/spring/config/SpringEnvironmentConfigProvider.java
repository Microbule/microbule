package org.microbule.spring.config;

import java.util.stream.Stream;

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

    public static final String NAME = "spring";

    @Autowired
    private Environment environment;

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        Config base = new SpringEnvironmentConfig(environment);
        return Stream.of(path).reduce(base, Config::group, (left, right) -> right);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }
}
