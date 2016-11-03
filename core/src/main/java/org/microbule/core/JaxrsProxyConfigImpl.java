package org.microbule.core;

import java.util.Map;

import org.microbule.spi.JaxrsProxyConfig;

public class JaxrsProxyConfigImpl extends JaxrsObjectConfigImpl implements JaxrsProxyConfig {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyConfigImpl(Class<?> serviceInterface, String baseAddress, Map<String, Object> properties) {
        super(serviceInterface, baseAddress, properties);
    }
}
