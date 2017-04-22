package org.microbule.osgi;

import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

public class JaxrsServerDecoratorRegistrar extends JaxrsServiceDecoratorRegistrar<JaxrsServerDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerDecoratorRegistrar(BundleContext bundleContext, DefaultJaxrsServerFactory factory) {
        super(bundleContext, JaxrsServerDecorator.class, factory);
        start();
    }
}
