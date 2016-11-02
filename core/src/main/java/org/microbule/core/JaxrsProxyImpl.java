package org.microbule.core;

import java.util.Map;

import org.microbule.spi.JaxrsProxy;

public class JaxrsProxyImpl extends JaxrsObjectImpl implements JaxrsProxy {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyImpl(Class<?> serviceInterface, Map<String, String> properties) {
        super(serviceInterface, properties::get);
    }
}
