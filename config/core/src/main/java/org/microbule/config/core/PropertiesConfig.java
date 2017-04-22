package org.microbule.config.core;

import java.util.Optional;
import java.util.Properties;

import org.microbule.config.api.Config;

public class PropertiesConfig implements Config {

    private static final String DEFAULT_SEPARATOR = ".";
    private final Properties values;
    private final String groupPrefix;
    private final String separator;

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
        this.values = values;
        this.groupPrefix = groupPrefix;
        this.separator = separator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public PropertiesConfig group(String groupKey) {
        return new PropertiesConfig(values, qualify(groupKey), separator);
    }

    @Override
    public Optional<String> value(String key) {
        return Optional.ofNullable(values.getProperty(qualify(key)));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public PropertiesConfig addValue(String key, String value) {
        values.put(qualify(key), value);
        return this;
    }

    private String qualify(String key) {
        return Optional.ofNullable(groupPrefix).map(prefix -> prefix + separator + key).orElse(key);
    }
}
