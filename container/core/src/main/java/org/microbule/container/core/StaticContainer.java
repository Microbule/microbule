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
            pluginFound(registration);
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
        pluginListeners.forEach(this::pluginFound);
        servers().forEach(def -> serverListeners.forEach(listener -> listener.registerServer(def)));
    }

    private <B> void pluginFound(PluginListenerRegistration<B> registration) {
        plugins(registration.getBeanType()).forEach(bean -> registration.getListener().registerPlugin(bean));
    }
}
