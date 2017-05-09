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
