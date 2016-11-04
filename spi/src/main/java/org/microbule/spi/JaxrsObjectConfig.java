package org.microbule.spi;

import java.util.function.Function;

public interface JaxrsObjectConfig {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a JAX-RS client provider
     *
     * @param provider the provider
     */
    void addProvider(Object provider);

    /**
     * Returns the base address of the JAX-RS service.
     *
     * @return the base address
     */
    String getBaseAddress();

    Boolean getBooleanProperty(String key);

    Double getDoubleProperty(String key);

    Integer getIntProperty(String key);

    String getProperty(String key);

    String getProperty(String key, String Value);

    <T> T getProperty(String key, Function<String, T> xform);

    <T> T getProperty(String key, Function<String, T> xform, T Value);

    /**
     * Returns the JAX-RS service interface
     *
     * @return the service interface
     */
    Class<?> getServiceInterface();

    /**
     * Returns the value of a boolean property
     *
     * @param key the property key
     * @return whether or not the value of the property is 'true' (case insensitive)
     */
    boolean isTrue(String key);
}
