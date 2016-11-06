package org.microbule.osgi;

import org.microbule.core.JaxrsProxyFactoryImpl;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;
import org.osgi.framework.BundleContext;

public class JaxrsProxyDecoratorRegistrar extends JaxrsObjectDecoratorRegistrar<JaxrsProxyConfig,JaxrsProxyDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyDecoratorRegistrar(BundleContext bundleContext, JaxrsProxyFactoryImpl factory) {
        super(bundleContext, JaxrsProxyDecorator.class, factory);
        start();
    }
}
