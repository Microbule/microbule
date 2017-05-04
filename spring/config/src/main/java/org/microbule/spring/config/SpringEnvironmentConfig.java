package org.microbule.spring.config;

import java.util.Optional;

import org.microbule.config.api.Config;
import org.springframework.core.env.Environment;

public class SpringEnvironmentConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String SEPARATOR = ".";
    private final Environment environment;
    private final String groupPrefix;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public SpringEnvironmentConfig(Environment environment) {
        this(environment, null);
    }

    private SpringEnvironmentConfig(Environment environment, String groupPrefix) {
        this.environment = environment;
        this.groupPrefix = groupPrefix;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        return new SpringEnvironmentConfig(environment, qualify(keyPrefix));
    }

    @Override
    public Optional<String> value(String key) {
        return Optional.ofNullable(environment.getProperty(qualify(key)));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private String qualify(String key) {
        return Optional.ofNullable(groupPrefix).map(prefix -> prefix + SEPARATOR + key).orElse(key);
    }
}
