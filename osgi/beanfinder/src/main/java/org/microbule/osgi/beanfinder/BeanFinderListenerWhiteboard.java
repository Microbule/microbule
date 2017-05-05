package org.microbule.osgi.beanfinder;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.microbule.beanfinder.api.BeanFinderListener;
import org.osgi.framework.BundleContext;

public class BeanFinderListenerWhiteboard<B> extends AbstractWhiteboard<B, B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final BeanFinderListener<B> listener;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public BeanFinderListenerWhiteboard(BundleContext bundleContext, Class<B> beanType, BeanFinderListener<B> listener) {
        super(bundleContext, beanType);
        this.listener = listener;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected B addService(B service, ServiceProperties props) {
        if (listener.beanFound(service)) {
            return service;
        }
        return null;
    }

    @Override
    protected void removeService(B service, B tracked) {
        listener.beanLost(service);
    }
}
