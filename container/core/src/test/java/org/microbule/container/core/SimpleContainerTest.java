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

package org.microbule.container.core;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.microbule.test.core.hello.HelloServiceImpl;

public class SimpleContainerTest extends MockObjectTestCase {

    @Test
    public void testPluginList() {
        final SimpleContainer container = new SimpleContainer();
        container.addBean("Hello");
        container.addBean("World");

        final List<String> strings = container.pluginList(String.class);
        assertTrue(strings.isEmpty());

        container.initialize();
        assertEquals(Arrays.asList("Hello", "World"), strings);

        container.addBean("Microbule");

        assertEquals(Arrays.asList("Hello", "World", "Microbule"), container.pluginList(String.class));
    }

    @Test
    public void testSortedSet() {
        final SimpleContainer container = new SimpleContainer();
        final SortedSet<String> strings = container.pluginSortedSet(String.class);

        container.addBean("a");
        container.addBean("b");
        container.addBean("c");
        container.initialize();

        assertEquals(Sets.newTreeSet(Lists.newArrayList("a", "b", "c")), strings);
    }

    @Test
    public void testMap() {
        final SimpleContainer container = new SimpleContainer();
        final Map<Integer, String> map = container.pluginMap(String.class, String::length);

        container.addBean("a");
        container.addBean("aa");
        container.addBean("aaa");
        container.initialize();

        assertEquals("a", map.get(1));
        assertEquals("aa", map.get(2));
        assertEquals("aaa", map.get(3));
    }

    @Test
    public void testRef() {
        final SimpleContainer container = new SimpleContainer();

        final AtomicReference<String> ref = container.pluginReference(String.class, "thedefault");
        assertEquals("thedefault", ref.get());

        container.addBean("hello");

        container.initialize();
        assertEquals("hello", ref.get());
    }

    @Test
    public void testServers() {
        final SimpleContainer container = new SimpleContainer();
        final HelloServiceImpl impl = new HelloServiceImpl();
        container.addBean(impl);

        final CollectingServerListener collector1 = new CollectingServerListener();
        container.addServerListener(collector1);
        container.initialize();

        assertEquals(1, collector1.definitions.size());

        final ServerDefinition def = collector1.definitions.get(0);
        assertNotNull(def.id());
        assertEquals(impl, def.serviceImplementation());
        assertEquals(HelloService.class, def.serviceInterface());

        final CollectingServerListener collector2 = new CollectingServerListener();
        container.addServerListener(collector2);
        assertEquals(1, collector2.definitions.size());


    }

    private static class CollectingServerListener implements ServerListener {
        private final List<ServerDefinition> definitions = new LinkedList<>();

        @Override
        public void registerServer(ServerDefinition serverDefinition) {
            definitions.add(serverDefinition);
        }

        @Override
        public void unregisterServer(String id) {

        }
    }

}