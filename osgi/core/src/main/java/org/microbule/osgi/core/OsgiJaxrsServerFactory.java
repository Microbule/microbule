package org.microbule.osgi.core;

import org.microbule.api.JaxrsServer;
import org.microbule.config.api.Config;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.osgi.beanfinder.OsgiBeanFinder;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

public class OsgiJaxrsServerFactory extends DefaultJaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final QuietTimeLatch latch;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OsgiJaxrsServerFactory(BundleContext bundleContext, QuietTimeLatch latch) {
        super(new OsgiBeanFinder(bundleContext));
        this.latch = latch;
        start();
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(JaxrsServerDecorator bean) {
        if (super.beanFound(bean)) {
            latch.updated();
            return true;
        }
        return false;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, Config config) {
        latch.await();
        return super.createJaxrsServer(serviceInterface, serviceImplementation, config);
    }
}
