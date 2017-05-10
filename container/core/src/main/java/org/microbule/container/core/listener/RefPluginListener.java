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

import java.util.concurrent.atomic.AtomicReference;

import org.microbule.container.api.PluginListener;

public class RefPluginListener<B> implements PluginListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<B> pluginRef;
    private final B defaultValue;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RefPluginListener(AtomicReference<B> pluginRef, B defaultValue) {
        this.pluginRef = pluginRef;
        this.defaultValue = defaultValue;
    }

//----------------------------------------------------------------------------------------------------------------------
// PluginListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean registerPlugin(B plugin) {
        return pluginRef.compareAndSet(defaultValue, plugin);
    }

    @Override
    public void unregisterPlugin(B bean) {
        pluginRef.compareAndSet(bean, defaultValue);
    }
}
