package org.microbule.spi;

public interface JaxrsServer {
    Class<?> getServiceInterface();
    void addProvider(Object provider);
    Object getProperty(String name);
    <T> T getProperty(String name, Class<T> expectedType);
}
