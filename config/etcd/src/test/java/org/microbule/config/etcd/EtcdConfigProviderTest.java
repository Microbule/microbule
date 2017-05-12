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

package org.microbule.config.etcd;

import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.core.GsonServiceImpl;
import org.microbule.gson.decorator.GsonDecorator;
import org.microbule.test.server.JaxrsServerTestCase;

public class EtcdConfigProviderTest extends JaxrsServerTestCase<MockEtcdService> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<EtcdNode> root = new AtomicReference<>();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new GsonDecorator(new GsonServiceImpl(container)));
    }

    @Override
    protected MockEtcdService createImplementation() {
        return new MockEtcdServiceImpl(root);
    }

    @Before
    public void setBaseAddress() {
        System.setProperty("microbule.etcd.baseAddress", getBaseAddress());
    }
    @Test
    public void testConfig() {
        final EtcdConfigProvider provider = new EtcdConfigProvider();
        assertEquals("etcd", provider.name());
        assertEquals(ConfigProvider.EXTERNAL_PRIORITY, provider.priority());

        root.set(parseResponse("/response.json", EtcdResponse.class).getNode());

        final Config config = provider.getConfig("one", "two");
        assertEquals("Microbule", config.value("three").get());
        assertEquals("three, sir", config.value("five").get());
    }

    private <T> T parseResponse(String resourceName, Class<T> type) {
        return new Gson().fromJson(new InputStreamReader(getClass().getResourceAsStream(resourceName), Charsets.UTF_8), type);
    }

}