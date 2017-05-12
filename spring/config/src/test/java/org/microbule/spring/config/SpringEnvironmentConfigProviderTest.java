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

package org.microbule.spring.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.test.core.MockObjectTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MicrobuleConfig.class)
@TestPropertySource("classpath:application.properties")
public class SpringEnvironmentConfigProviderTest extends MockObjectTestCase {

    @Autowired
    private SpringEnvironmentConfigProvider provider;

    @Test
    public void testConfig() {
        assertEquals("spring", provider.name());
        assertEquals(ConfigProvider.DEFAULT_PRIORITY, provider.priority());

        final Config config = provider.getConfig("one", "two");
        assertEquals("hello", config.value("three").get());
        assertEquals("three, sir", config.value("five").get());
    }

}