package org.microbule.core;

import java.util.Map;

import org.microbule.spi.JaxrsServerConfig;

public class JaxrsServerConfigImpl extends JaxrsObjectConfigImpl implements JaxrsServerConfig {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerConfigImpl(Class<?> serviceInterface, String baseAddress, Map<String,Object> properties) {
        super(serviceInterface, baseAddress, properties);
    }
}
