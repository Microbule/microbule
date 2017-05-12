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

import java.util.Comparator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class DefaultConfigBuilderFactoryTest extends MockObjectTestCase  {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ConfigProvider provider1;

    @Mock
    private ConfigProvider provider2;

    @Mock
    private MicrobuleContainer container;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @SuppressWarnings("unchecked")
    public void testWithNoConfigProviders() {
        final MapConfig config1 = new MapConfig();
        config1.addValue("a.one", "config1.a1");
        config1.addValue("b.one", "config1.b1");
        config1.addValue("b.two", "config1.b2");

        when(provider1.getConfig("a")).thenReturn(config1.filtered("a"));
        when(provider1.getConfig("b")).thenReturn(config1.filtered("b"));
        when(provider1.name()).thenReturn("provider1");
        when(provider1.priority()).thenReturn(ConfigProvider.DEFAULT_PRIORITY);

        final MapConfig config2 = new MapConfig();
        config2.addValue("a.one", "config2.a1");
        config2.addValue("a.three", "config2.a3");
        config2.addValue("b.one", "config2.b1");
        config2.addValue("b.three", "config2.b3");

        when(provider2.getConfig("a")).thenReturn(config2.filtered("a"));
        when(provider2.getConfig("b")).thenReturn(config2.filtered("b"));
        when(provider2.name()).thenReturn("provider2");
        when(provider2.priority()).thenReturn(ConfigProvider.EXTERNAL_PRIORITY);


        when(container.pluginSortedSet(same(ConfigProvider.class), any())).thenAnswer(invocation -> {
            SortedSet<ConfigProvider> set = new ConcurrentSkipListSet<>((Comparator<ConfigProvider>)invocation.getArgument(1));
            set.add(provider1);
            set.add(provider2);
            return set;
        });

        final DefaultConfigBuilderFactory configService = new DefaultConfigBuilderFactory(container);

        final Config config = configService.createBuilder()
                .withPath("a")
                .withPath("b")
                .build();

        assertEquals("config1.a1", config.value("one").get());
        assertEquals("config1.b2", config.value("two").get());
        assertEquals("config2.a3", config.value("three").get());
    }
}