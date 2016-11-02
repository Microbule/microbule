package org.microbule.core;

import org.microbule.spi.JaxrsServer;
import org.osgi.framework.ServiceReference;

public class JaxrsServerImpl extends JaxrsObjectImpl implements JaxrsServer {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerImpl(Class<?> serviceInterface, ServiceReference<?> serviceReference) {
        super(serviceInterface, serviceReference::getProperty);
    }
}
