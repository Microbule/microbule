package org.microbule.osgi.beanfinder;

import java.util.concurrent.atomic.AtomicLong;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.microbule.beanfinder.api.BeanFinderListener;
import org.osgi.framework.BundleContext;

public class BeanFinderListenerWhiteboard<B> extends AbstractWhiteboard<B, B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final BeanFinderListener<B> listener;
    private final AtomicLong lastUpdated;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public BeanFinderListenerWhiteboard(BundleContext bundleContext, Class<B> beanType, BeanFinderListener<B> listener, AtomicLong lastUpdated) {
        super(bundleContext, beanType);
        this.listener = listener;
        this.lastUpdated = lastUpdated;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected B addService(B service, ServiceProperties props) {
        if (listener.beanFound(service)) {
            lastUpdated.set(System.nanoTime());
            return service;
        }
        return null;
    }

    @Override
    protected void removeService(B service, B tracked) {
        listener.beanLost(service);
    }
}
