package org.microbule.config.core;

import java.util.Optional;
import java.util.Properties;

import org.microbule.config.api.Config;

public class PropertiesConfig extends AbstractConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Properties values;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public PropertiesConfig() {
        this(new Properties(), null, DEFAULT_SEPARATOR);
    }

    public PropertiesConfig(Properties values) {
        this(new Properties(values), null, DEFAULT_SEPARATOR);
    }

    public PropertiesConfig(Properties values, String separator) {
        this(new Properties(values), null, separator);
    }

    private PropertiesConfig(Properties values, String groupPrefix, String separator) {
        super(groupPrefix, separator);
        this.values = values;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public PropertiesConfig addValue(String key, String value) {
        values.put(qualify(key), value);
        return this;
    }

    @Override
    protected Config qualifiedConfig(String qualifiedGroupPrefix, String separator) {
        return new PropertiesConfig(values, qualifiedGroupPrefix, separator);
    }

    @Override
    protected Optional<String> qualifiedValue(String qualifiedKey) {
        return Optional.ofNullable(values.getProperty(qualifiedKey));
    }
}
