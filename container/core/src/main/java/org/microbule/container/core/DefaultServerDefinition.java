package org.microbule.container.core;

import org.microbule.config.api.Config;
import org.microbule.config.core.EmptyConfig;
import org.microbule.container.api.ServerDefinition;

public class DefaultServerDefinition implements ServerDefinition {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String id;
    private final Class<?> serviceInterface;
    private final Object serviceImplementation;
    private final Config customConfig;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultServerDefinition(String id, Class<?> serviceInterface, Object serviceImplementation) {
        this(id, serviceInterface, serviceImplementation, EmptyConfig.INSTANCE);
    }

    public DefaultServerDefinition(String id, Class<?> serviceInterface, Object serviceImplementation, Config customConfig) {
        this.id = id;
        this.serviceInterface = serviceInterface;
        this.serviceImplementation = serviceImplementation;
        this.customConfig = customConfig;
    }

//----------------------------------------------------------------------------------------------------------------------
// ServerDefinition Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config customConfig() {
        return customConfig;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Object serviceImplementation() {
        return serviceImplementation;
    }

    @Override
    public Class<?> serviceInterface() {
        return serviceInterface;
    }
}
