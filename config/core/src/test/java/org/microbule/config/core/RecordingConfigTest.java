package org.microbule.config.core;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.test.core.MicrobuleTestCase;

public class RecordingConfigTest extends MicrobuleTestCase {

    @Test
    public void testWithEmpty() {
        final RecordingConfig recording = new RecordingConfig(new MapConfig());
        recording.value("foo");
        recording.value("bar");
        recording.filtered("baz");
        final JsonObject json = recording.getRecordedJson();
        assertEquals(JsonNull.INSTANCE, json.get("foo"));
        assertEquals(JsonNull.INSTANCE, json.get("bar"));
        assertEquals(new JsonObject(), json.get("baz"));
    }

    @Test
    public void testRecording() {
        final MapConfig mapConfig = new MapConfig();
        mapConfig.addValue("foo", "bar");
        mapConfig.addValue("theint", "12345");
        ((MapConfig) mapConfig.filtered("nested")).addValue("hello", "world");
        ((MapConfig) mapConfig.filtered("nested", "nested2")).addValue("hello", "world");
        final RecordingConfig recording = new RecordingConfig(mapConfig);

        recording.value("foo");
        recording.value("baz");
        recording.integerValue("theint");
        final Config nested = recording.filtered("nested");
        nested.value("hello");
        nested.value("bogus");
        nested.filtered("nested2").value("hello");

        final JsonObject json = recording.getRecordedJson();

        assertEquals(new JsonPrimitive("bar"), json.get("foo"));
        assertEquals(new JsonPrimitive("12345"), json.get("theint"));
        assertEquals(JsonNull.INSTANCE, json.get("baz"));

        JsonObject nestedJson = json.getAsJsonObject("nested");
        assertEquals(new JsonPrimitive("world"), nestedJson.get("hello"));
        assertEquals(JsonNull.INSTANCE, nestedJson.get("bogus"));

        JsonObject nestedJson2 = nestedJson.getAsJsonObject("nested2");
        assertEquals(new JsonPrimitive("world"), nestedJson2.get("hello"));
    }
}