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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.google.common.collect.MapMaker;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.core.listener.CollectionPluginListener;
import org.microbule.container.core.listener.MapPluginListener;
import org.microbule.container.core.listener.RefPluginListener;

public abstract class AbstractContainer implements MicrobuleContainer {
//----------------------------------------------------------------------------------------------------------------------
// MicrobuleContainer Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> List<B> pluginList(Class<B> beanType) {
        final List<B> list = new CopyOnWriteArrayList<>();
        addPluginListener(beanType, new CollectionPluginListener<>(list));
        return list;
    }

    @Override
    public <K, B> Map<K, B> pluginMap(Class<B> beanType, Function<B, K> keyFunction) {
        final Map<K, B> map = new MapMaker().makeMap();
        addPluginListener(beanType, new MapPluginListener<>(map, keyFunction));
        return map;
    }

    @Override
    public <B> AtomicReference<B> pluginReference(Class<B> beanType, B defaultValue) {
        final AtomicReference<B> ref = new AtomicReference<>(defaultValue);
        addPluginListener(beanType, new RefPluginListener<>(ref, defaultValue));
        return ref;
    }

    @Override
    public <B extends Comparable<? super B>> SortedSet<B> pluginSortedSet(Class<B> pluginType) {
        return pluginSortedSet(pluginType, Comparator.naturalOrder());
    }

    @Override
    public <B> SortedSet<B> pluginSortedSet(Class<B> beanType, Comparator<? super B> comparator) {
        final SortedSet<B> beans = new ConcurrentSkipListSet<>(comparator);
        addPluginListener(beanType, new CollectionPluginListener<>(beans));
        return beans;
    }
}
