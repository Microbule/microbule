package org.microbule.spring.config;

import java.util.Optional;

import org.microbule.config.api.Config;
import org.springframework.core.env.Environment;

public class SpringEnvironmentConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Environment environment;
    private final String prefix;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public SpringEnvironmentConfig(Environment environment) {
        this(environment, "");
    }

    private SpringEnvironmentConfig(Environment environment, String prefix) {
        this.environment = environment;
        this.prefix = prefix;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        return new SpringEnvironmentConfig(environment, prefix + "." + keyPrefix);
    }

    @Override
    public Optional<String> value(String key) {
        return Optional.ofNullable(environment.getProperty(prefix + "." + key));
    }
}
