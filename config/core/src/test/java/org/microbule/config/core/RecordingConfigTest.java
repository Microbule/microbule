package org.microbule.config.core;

import javax.json.Json;
import javax.json.JsonObject;

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
        assertNull(json.get("foo"));
        assertNull(json.get("bar"));
        assertEquals(Json.createObjectBuilder().build(), json.get("baz"));
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

        assertEquals(Json.createValue("bar"), json.get("foo"));
        assertEquals(Json.createValue("12345"), json.get("theint"));
        assertNull(json.get("baz"));

        JsonObject nestedJson = json.getJsonObject("nested");
        assertEquals(Json.createValue("world"), nestedJson.get("hello"));
        assertNull(nestedJson.get("bogus"));

        JsonObject nestedJson2 = nestedJson.getJsonObject("nested2");
        assertEquals(Json.createValue("world"), nestedJson2.get("hello"));
    }
}