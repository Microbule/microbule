package org.microbule.config.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.microbule.config.api.Config;

public class MapConfig extends AbstractConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<String, String> values;

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
        super(groupPrefix, separator);
        this.values = values;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public MapConfig addValue(String key, String value) {
        values.put(qualify(key), value);
        return this;
    }

    @Override
    protected Config qualifiedConfig(String qualifiedGroupPrefix, String separator) {
        return new MapConfig(values, qualifiedGroupPrefix, separator);
    }

    @Override
    protected Optional<String> qualifiedValue(String qualifiedKey) {
        return Optional.ofNullable(values.get(qualifiedKey));
    }
}
