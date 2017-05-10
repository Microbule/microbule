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

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.api.Config;

public class CompositeConfigTest extends Assert {
    @Test
    public void testValue() {
        MapConfig config1 = new MapConfig();
        config1.addValue("foo", "bar");
        MapConfig config2 = new MapConfig();
        config2.addValue("foo", "baz");
        config2.addValue("hello", "world");

        final CompositeConfig config = new CompositeConfig(config1, config2);
        assertEquals("bar", config.value("foo").get());
        assertEquals("world", config.value("hello").get());
    }

    @Test
    public void testGroup() {
        MapConfig config1 = new MapConfig();
        config1.addValue("a.foo", "bar");
        MapConfig config2 = new MapConfig();
        config2.addValue("a.foo", "baz");
        config2.addValue("a.hello", "world");

        final Config config = new CompositeConfig(config1, config2).group("a");
        assertEquals("bar", config.value("foo").get());
        assertEquals("world", config.value("hello").get());
    }
}