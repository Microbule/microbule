package org.microbule.osgi;

import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

public class JaxrsServerDecoratorRegistrar extends JaxrsObjectDecoratorRegistrar<JaxrsServerConfig,JaxrsServerDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerDecoratorRegistrar(BundleContext bundleContext, JaxrsServerFactoryImpl factory) {
        super(bundleContext, JaxrsServerDecorator.class, factory);
        start();
    }
}
