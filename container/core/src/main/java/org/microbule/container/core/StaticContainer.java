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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.microbule.container.api.PluginListener;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;

public abstract class StaticContainer extends AbstractContainer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<PluginListenerRegistration<?>> pluginListeners = new CopyOnWriteArrayList<>();
    private final List<ServerListener> serverListeners = new CopyOnWriteArrayList<>();

    private final AtomicBoolean initialized = new AtomicBoolean(false);

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract <B> Iterable<B> plugins(Class<B> beanType);

    protected abstract Iterable<ServerDefinition> servers();

//----------------------------------------------------------------------------------------------------------------------
// MicrobuleContainer Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> void addPluginListener(Class<B> beanType, PluginListener<B> listener) {
        final PluginListenerRegistration<B> registration = new PluginListenerRegistration<>(beanType, listener);
        pluginListeners.add(registration);
        if (initialized.get()) {
            registerPlugin(registration);
        }
    }

    @Override
    public void addServerListener(ServerListener listener) {
        serverListeners.add(listener);
        if (initialized.get()) {
            servers().forEach(listener::registerServer);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void initialize() {
        initialized.set(true);
        pluginListeners.forEach(this::registerPlugin);
        servers().forEach(def -> serverListeners.forEach(listener -> listener.registerServer(def)));
    }

    protected void beanRemoved(Object bean) {
        for (PluginListenerRegistration<?> pluginListener : pluginListeners) {
            if (pluginListener.getPluginType().isInstance(bean)) {
                unregisterPlugin(pluginListener, bean);
            }
        }
    }

    private <B> void registerPlugin(PluginListenerRegistration<B> registration) {
        plugins(registration.getPluginType()).forEach(bean -> registration.getListener().registerPlugin(bean));
    }

    private <B> void unregisterPlugin(PluginListenerRegistration<B> registration, Object bean) {
        registration.getListener().unregisterPlugin(registration.getPluginType().cast(bean));
    }

    protected void serverRemoved(String id) {
        serverListeners.forEach(serverListener -> serverListener.unregisterServer(id));
    }
}
