package org.microbule.config.core;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
        this(delegate, new JsonObject());
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
            JsonObject sub = new JsonObject();
            filteredJson.add(path, sub);
            filteredJson = sub;
        }
        return new RecordingConfig(delegate.filtered(paths), filteredJson);
    }

    @Override
    public Optional<String> value(String key) {
        final Optional<String> value = delegate.value(key);
        recordedJson.add(key, value.<JsonElement>map(JsonPrimitive::new).orElse(JsonNull.INSTANCE));
        return value;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public JsonObject getRecordedJson() {
        return recordedJson;
    }
}
