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

import java.util.Collection;

import org.microbule.container.api.PluginListener;


public class CollectionPluginListener<B> implements PluginListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Collection<B> plugins;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CollectionPluginListener(Collection<B> plugins) {
        this.plugins = plugins;
    }

//----------------------------------------------------------------------------------------------------------------------
// PluginListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean registerPlugin(B plugin) {
        return plugins.add(plugin);
    }

    @Override
    public void unregisterPlugin(B bean) {
        plugins.remove(bean);
    }
}
