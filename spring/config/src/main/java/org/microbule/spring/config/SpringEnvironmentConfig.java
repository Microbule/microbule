package org.microbule.spring.config;

import java.util.Optional;

import org.microbule.config.api.Config;
import org.microbule.config.core.AbstractConfig;
import org.springframework.core.env.Environment;

public class SpringEnvironmentConfig extends AbstractConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Environment environment;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public SpringEnvironmentConfig(Environment environment) {
        this(environment, null, DEFAULT_SEPARATOR);
    }

    private SpringEnvironmentConfig(Environment environment, String groupPrefix, String separator) {
        super(groupPrefix, separator);
        this.environment = environment;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Config qualifiedConfig(String qualifiedGroupPrefix, String separator) {
        return new SpringEnvironmentConfig(environment, qualifiedGroupPrefix, separator);
    }

    @Override
    protected Optional<String> qualifiedValue(String qualifiedKey) {
        return Optional.ofNullable(environment.getProperty(qualifiedKey));
    }
}
