package org.microbule.osgi.container;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.microbule.container.api.PluginListener;
import org.osgi.framework.BundleContext;

public class PluginListenerWhiteboard<P> extends AbstractWhiteboard<P, P> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final PluginListener<P> pluginListener;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public PluginListenerWhiteboard(BundleContext bundleContext, Class<P> pluginType, PluginListener<P> pluginListener) {
        super(bundleContext, pluginType);
        this.pluginListener = pluginListener;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected P addService(P service, ServiceProperties props) {
        if (pluginListener.registerPlugin(service)) {
            return service;
        }
        return null;
    }

    @Override
    protected void removeService(P service, P tracked) {
        pluginListener.unregisterPlugin(service);
    }
}
