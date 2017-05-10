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

package org.microbule.container.core.listener;

import java.util.Map;
import java.util.function.Function;

import org.microbule.container.api.PluginListener;

public class MapPluginListener<K, B> implements PluginListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<K, B> pluginMap;
    private final Function<B, K> keyFunction;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MapPluginListener(Map<K, B> pluginMap, Function<B, K> keyFunction) {
        this.pluginMap = pluginMap;
        this.keyFunction = keyFunction;
    }

//----------------------------------------------------------------------------------------------------------------------
// PluginListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean registerPlugin(B plugin) {
        return pluginMap.putIfAbsent(keyFunction.apply(plugin), plugin) == null;
    }

    @Override
    public void unregisterPlugin(B bean) {
        pluginMap.remove(keyFunction.apply(bean));
    }
}
