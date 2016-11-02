package org.microbule.core;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.microbule.spi.JaxrsObject;

public abstract class JaxrsObjectImpl implements JaxrsObject {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final Function<String, Object> propertyFunction;
    private final List<Object> providers = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsObjectImpl(Class<?> serviceInterface, Function<String, Object> propertyFunction) {
        this.serviceInterface = serviceInterface;
        this.propertyFunction = propertyFunction;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObject Implementation
//----------------------------------------------------------------------------------------------------------------------

    public void addProvider(Object provider) {
        providers.add(provider);
    }

    @Override
    public final String getProperty(String key) {
        Object value = propertyFunction.apply(key);
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
