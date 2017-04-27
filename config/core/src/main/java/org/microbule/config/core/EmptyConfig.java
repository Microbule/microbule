package org.microbule.config.core;

import java.util.Optional;

import org.microbule.config.api.Config;

public class EmptyConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final Config INSTANCE = new EmptyConfig();

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public Config group(String keyPrefix) {
        return this;
    }

    @Override
    public Optional<String> value(String key) {
        return Optional.empty();
    }
}
