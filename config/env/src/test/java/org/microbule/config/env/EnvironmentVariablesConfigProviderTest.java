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

package org.microbule.config.env;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.api.Config;

public class EnvironmentVariablesConfigProviderTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetServerConfig() {
        Map<String, String> env = new HashMap<>();
        env.put("one_two_foo", "bar");
        EnvironmentVariablesConfigProvider provider = new EnvironmentVariablesConfigProvider(() -> env);

        final Config config = provider.getConfig("one", "two");
        assertEquals("bar", config.value("foo").get());
    }
}