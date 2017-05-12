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

package org.microbule.osgi.container;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Rule;
import org.junit.Test;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;
import org.microbule.test.osgi.OsgiRule;
import org.osgi.framework.ServiceRegistration;

import static org.microbule.test.osgi.ServicePropsBuilder.props;

public class OsgiContainerTest extends MockObjectTestCase {

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

    @Test
    public void testPlugins() throws Exception {
        osgiRule.registerService(HelloService.class, new HelloServiceImpl(), props().with("microbule.server", "true"));

        final OsgiContainer container = new OsgiContainer(osgiRule.getBundleContext(), 100);
        final List<HelloService> plugins = container.pluginList(HelloService.class);

        final List<ServerDefinition> serverDefinitions = new LinkedList<>();
        container.addServerListener(new ServerListener() {
            @Override
            public void registerServer(ServerDefinition serverDefinition) {
                serverDefinitions.add(serverDefinition);
            }

            @Override
            public void unregisterServer(String id) {
                serverDefinitions.removeIf(def -> def.id().equals(id));
            }
        });

        Thread.sleep(150);
        final ServiceRegistration<HelloServiceImpl> registration = osgiRule.registerService(HelloService.class, new HelloServiceImpl(), props().with("microbule.server", "true"));

        assertEquals(2, plugins.size());
        assertEquals(2, serverDefinitions.size());

        registration.unregister();

        assertEquals(1, plugins.size());
        assertEquals(1, serverDefinitions.size());
    }


    @Test
    public void testPluginsWhenRejected() throws Exception {
        final HelloServiceImpl service1 = new HelloServiceImpl();
        final ServiceRegistration<HelloServiceImpl> registration1 = osgiRule.registerService(HelloService.class, service1, props().with("microbule.server", "true"));

        final OsgiContainer container = new OsgiContainer(osgiRule.getBundleContext(), 100);
        final HelloServiceImpl defaultVal = new HelloServiceImpl();
        final AtomicReference<HelloService> ref = container.pluginReference(HelloService.class, defaultVal);
        assertEquals(service1, ref.get());
        final HelloServiceImpl service2 = new HelloServiceImpl();
        final ServiceRegistration<HelloServiceImpl> registration2 = osgiRule.registerService(HelloService.class, service2, props().with("microbule.server", "true"));
        assertEquals(service1, ref.get());
        registration2.unregister();
        assertEquals(service1, ref.get());
        registration1.unregister();
        assertEquals(defaultVal, ref.get());
    }

}