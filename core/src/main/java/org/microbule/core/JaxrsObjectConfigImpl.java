package org.microbule.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.microbule.spi.JaxrsObjectConfig;

public class JaxrsObjectConfigImpl implements JaxrsObjectConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final String baseAddress;
    private final Map<String, Object> properties;
    private final List<Object> providers = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsObjectConfigImpl(Class<?> serviceInterface, String baseAddress, Map<String, Object> properties) {
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

    public Boolean getBooleanProperty(String key) {
        return getProperty(key, Boolean::parseBoolean);
    }

    public Double getDoubleProperty(String key) {
        return getProperty(key, Double::parseDouble);
    }

    public Integer getIntProperty(String key) {
        return getProperty(key, Integer::parseInt);
    }

    public String getProperty(String key, String defaultValue) {
        final String value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    public <T> T getProperty(String key, Function<String, T> xform) {
        final String value = getProperty(key);
        return value == null ? null : xform.apply(value);
    }

    public <T> T getProperty(String key, Function<String, T> xform, T defaultValue) {
        final T value = getProperty(key, xform);
        return value == null ? defaultValue : value;
    }


    /**
     * Returns the value of a boolean property
     *
     * @param key the property key
     * @return whether or not the value of the property is 'true' (case insensitive)
     */
    public boolean isTrue(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public List<Object> getProviders() {
        return providers;
    }
}
