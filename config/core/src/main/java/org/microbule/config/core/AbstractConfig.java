package org.microbule.config.core;

import java.util.Optional;

import org.microbule.config.api.Config;

public abstract class AbstractConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    protected static final String DEFAULT_SEPARATOR = ".";

    private final String groupPrefix;
    private final String separator;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AbstractConfig() {
        this(null, DEFAULT_SEPARATOR);
    }

    public AbstractConfig(String separator) {
        this(null, separator);
    }

    public AbstractConfig(String groupPrefix, String separator) {
        this.groupPrefix = groupPrefix;
        this.separator = separator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Config qualifiedConfig(String qualifiedGroupPrefix, String separator);

    protected abstract Optional<String> qualifiedValue(String qualifiedKey);

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        return qualifiedConfig(qualify(keyPrefix), separator);
    }

    @Override
    public Optional<String> value(String key) {
        return qualifiedValue(qualify(key));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected String qualify(String key) {
        return Optional.ofNullable(groupPrefix).map(prefix -> prefix + separator + key).orElse(key);
    }
}
