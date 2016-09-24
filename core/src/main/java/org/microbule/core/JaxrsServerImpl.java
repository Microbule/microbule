package org.microbule.core;

import java.util.LinkedList;
import java.util.List;

import org.microbule.spi.JaxrsServer;
import org.osgi.framework.ServiceReference;

public class JaxrsServerImpl implements JaxrsServer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final List<Object> providers = new LinkedList<>();
    private final ServiceReference<?> serviceReference;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerImpl(Class<?> serviceInterface, ServiceReference<?> serviceReference) {
        this.serviceInterface = serviceInterface;
        this.serviceReference = serviceReference;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServer Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void addProvider(Object provider) {
        providers.add(provider);
    }

    @Override
    public Object getProperty(String key) {
        return serviceReference.getProperty(key);
    }

    @Override
    public <T> T getProperty(String key, Class<T> expectedType) {
        Object value = serviceReference.getProperty(key);
        return expectedType.cast(value);
    }

    @Override
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
