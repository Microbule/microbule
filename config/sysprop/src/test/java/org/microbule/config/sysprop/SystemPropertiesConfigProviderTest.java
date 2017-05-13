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

package org.microbule.config.sysprop;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.core.ConfigUtils;

public class SystemPropertiesConfigProviderTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetConfig() {
        System.setProperty("one.two.foo", "bar");
        final SystemPropertiesConfigProvider provider = new SystemPropertiesConfigProvider();

        assertEquals("sysprop", provider.name());
        assertEquals(ConfigUtils.PRIORITY_SYSPROP, provider.priority());

        assertEquals("bar", provider.getConfig("one", "two").value("foo").get());
    }

    @Test
    public void testDefaultConstructor() {
        final SystemPropertiesConfigProvider provider = new SystemPropertiesConfigProvider();

        assertEquals(System.getProperty("user.dir"), provider.getConfig("user").value("dir").get());
    }
}