package org.microbule.container.core;


import org.microbule.container.api.PluginListener;

public class PluginListenerRegistration<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<B> beanType;
    private final PluginListener<B> listener;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public PluginListenerRegistration(Class<B> beanType, PluginListener<B> listener) {
        this.beanType = beanType;
        this.listener = listener;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public Class<B> getBeanType() {
        return beanType;
    }

    public PluginListener<B> getListener() {
        return listener;
    }
}
