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

package org.microbule.container.api;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface MicrobuleContainer {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    <B> void addPluginListener(Class<B> beanType, PluginListener<B> listener);

    void addServerListener(ServerListener listener);

    <B> List<B> pluginList(Class<B> beanType);

    <K, B> Map<K, B> pluginMap(Class<B> beanType, Function<B, K> keyFunction);

    <B> AtomicReference<B> pluginReference(Class<B> beanType, B defaultValue);

    <B extends Comparable<? super B>> SortedSet<B> pluginSortedSet(Class<B> pluginType);

    <B> SortedSet<B> pluginSortedSet(Class<B> beanType, Comparator<? super B> comparator);
}
