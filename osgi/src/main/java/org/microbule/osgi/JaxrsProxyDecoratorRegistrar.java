package org.microbule.osgi;

import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyDecorator;
import org.osgi.framework.BundleContext;

public class JaxrsProxyDecoratorRegistrar extends JaxrsServiceDecoratorRegistrar<JaxrsProxyDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyDecoratorRegistrar(BundleContext bundleContext, DefaultJaxrsProxyFactory factory) {
        super(bundleContext, JaxrsProxyDecorator.class, factory);
        start();
    }
}
