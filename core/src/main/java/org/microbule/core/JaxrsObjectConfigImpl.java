package org.microbule.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.microbule.spi.JaxrsObjectConfig;

public abstract class JaxrsObjectConfigImpl implements JaxrsObjectConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final String baseAddress;
    private final Map<String,Object> properties;
    private final List<Object> providers = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsObjectConfigImpl(Class<?> serviceInterface, String baseAddress, Map<String,Object> properties) {
        this.serviceInterface = serviceInterface;
        this.baseAddress = baseAddress;
        this.properties = properties;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectConfig Implementation
//----------------------------------------------------------------------------------------------------------------------

    public void addProvider(Object provider) {
        providers.add(provider);
    }

    @Override
    public String getBaseAddress() {
        return baseAddress;
    }

    @Override
    public final String getProperty(String key) {
        Object value = properties.get(key);
        return value == null ? null : String.valueOf(value);
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public List<Object> getProviders() {
        return providers;
    }
}
