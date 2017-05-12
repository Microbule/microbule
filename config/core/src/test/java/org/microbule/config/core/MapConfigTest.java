/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.config.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.api.Config;

public class MapConfigTest extends Assert {
    @Test
    public void testValue() {
        Map<String,String> values = new HashMap<>();
        values.put("foo", "bar");
        final MapConfig config = new MapConfig(values);
        assertEquals("bar", config.value("foo").get());
    }

    @Test
    public void testWithCustomSeparator() {
        Map<String,String> values = new HashMap<>();
        values.put("foo/bar", "baz");
        final MapConfig config = new MapConfig(values, "/");
        assertEquals("baz", config.filtered("foo").value("bar").get());
    }

    @Test
    public void testFilteredWithNoPaths() {
        Map<String,String> values = new HashMap<>();
        values.put("foo/bar", "baz");
        final Config config = new MapConfig(values, "/").filtered();
        assertEquals("baz", config.filtered("foo").value("bar").get());
    }
}