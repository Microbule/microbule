package org.microbule.core;

import java.util.function.Function;

import org.microbule.spi.JaxrsServerProperties;
import org.osgi.framework.ServiceReference;

public class JaxrsServerPropertiesImpl implements JaxrsServerProperties {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ServiceReference<?> reference;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerPropertiesImpl(ServiceReference<?> reference) {
        this.reference = reference;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerProperties Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public String getProperty(String key) {
        final Object value = reference.getProperty(key);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public <T> T getProperty(String key, Function<String, T> xform) {
        final String value = getProperty(key);
        return value == null ? null : xform.apply(value);
    }
}
