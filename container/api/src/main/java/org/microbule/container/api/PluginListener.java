package org.microbule.container.api;

public interface PluginListener<P> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    boolean registerPlugin(P plugin);

    void unregisterPlugin(P bean);
}
