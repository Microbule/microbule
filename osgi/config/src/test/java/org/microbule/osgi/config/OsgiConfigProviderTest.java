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

package org.microbule.osgi.config;

import java.util.Hashtable;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.core.ConfigUtils;
import org.microbule.test.core.MockObjectTestCase;

public class OsgiConfigProviderTest extends MockObjectTestCase {

    @Test
    public void testGetConfig() throws Exception {
        final OsgiConfigProvider provider = new OsgiConfigProvider();
        assertEquals("osgi", provider.name());
        assertEquals(ConfigUtils.DEFAULT_PRIORITY, provider.priority());

        Config config = provider.getConfig("foo", "bar");

        assertFalse(config.value("baz").isPresent());

        Hashtable<String,Object> props = new Hashtable<>();
        props.put("foo.bar.baz", "hello");
        provider.updated(props);

        config = provider.getConfig("foo", "bar");
        assertEquals("hello", config.value("baz").get());

        provider.updated(null);

        config = provider.getConfig("foo", "bar");
        assertFalse(config.value("baz").isPresent());
    }
}