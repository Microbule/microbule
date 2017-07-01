package org.microbule.config.core;

import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;

import org.microbule.config.api.Config;

public class RecordingConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final JsonObject recordedJson;
    private final Config delegate;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RecordingConfig(Config delegate) {
        this(delegate, Json.createObjectBuilder().build());
    }

    private RecordingConfig(Config delegate, JsonObject recordedJson) {
        this.delegate = delegate;
        this.recordedJson = recordedJson;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config filtered(String... paths) {
        JsonObject filteredJson = recordedJson;
        for (String path : paths) {
            JsonObject sub = Json.createObjectBuilder().build();
            filteredJson.put(path, sub);
            filteredJson = sub;
        }
        return new RecordingConfig(delegate.filtered(paths), filteredJson);
    }

    @Override
    public Optional<String> value(String key) {
        final Optional<String> value = delegate.value(key);
        recordedJson.put(key, value.map(Json::createValue).orElse(null));
        return value;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public JsonObject getRecordedJson() {
        return recordedJson;
    }
}
