package org.microbule.osgi.core;

import org.microbule.config.api.Config;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.osgi.beanfinder.OsgiBeanFinder;
import org.microbule.spi.JaxrsProxyDecorator;
import org.osgi.framework.BundleContext;

public class OsgiJaxrsProxyFactory extends DefaultJaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final QuietTimeLatch latch;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OsgiJaxrsProxyFactory(BundleContext bundleContext, QuietTimeLatch latch) {
        super(new OsgiBeanFinder(bundleContext));
        this.latch = latch;
        start();
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(JaxrsProxyDecorator bean) {
        if (super.beanFound(bean)) {
            latch.updated();
            return true;
        }
        return false;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, Config config) {
        latch.await();
        return super.createProxy(serviceInterface, config);
    }
}
