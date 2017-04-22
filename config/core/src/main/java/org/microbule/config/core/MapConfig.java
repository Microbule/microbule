package org.microbule.config.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.microbule.config.api.Config;

public class MapConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_SEPARATOR = ".";
    private final Map<String, String> values;
    private final String groupPrefix;
    private final String separator;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MapConfig() {
        this(new HashMap<>(), null, DEFAULT_SEPARATOR);
    }

    public MapConfig(Map<String, String> values) {
        this(new HashMap<>(values), null, DEFAULT_SEPARATOR);
    }

    public MapConfig(Map<String, String> values, String separator) {
        this(new HashMap<>(values), null, separator);
    }

    private MapConfig(Map<String, String> values, String groupPrefix, String separator) {
        this.values = values;
        this.groupPrefix = groupPrefix;
        this.separator = separator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public MapConfig group(String groupKey) {
        return new MapConfig(values, qualify(groupKey), separator);
    }

    @Override
    public Optional<String> value(String key) {
        return Optional.ofNullable(values.get(qualify(key)));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public MapConfig addValue(String key, String value) {
        values.put(qualify(key), value);
        return this;
    }

    private String qualify(String key) {
        return Optional.ofNullable(groupPrefix).map(prefix -> prefix + separator + key).orElse(key);
    }
}
