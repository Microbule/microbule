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
