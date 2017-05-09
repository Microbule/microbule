package org.microbule.core;

import java.util.Map;

import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsServiceDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.slf4j.Logger;

public abstract class JaxrsServiceDecoratorRegistry<T extends JaxrsServiceDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENABLED_PROPERTY = "enabled";
    private final Map<String, T> decoratorsMap;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDecoratorRegistry(Class<T> decoratorType, MicrobuleContainer container) {
        this.decoratorsMap = container.pluginMap(decoratorType, JaxrsServiceDecorator::name);
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Logger getLogger();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        decoratorsMap.forEach((name, decorator) -> {
            final Config decoratorConfig = config.group(name);
            if (decoratorConfig.booleanValue(ENABLED_PROPERTY).orElse(Boolean.TRUE)) {
                getLogger().info("Decorating {} service using decorator \"{}\".", descriptor.serviceInterface().getSimpleName(), name);
                decorator.decorate(descriptor, decoratorConfig);
            } else {
                getLogger().info("Skipping decorator \"{}\" for service {}.", name, descriptor.serviceInterface().getSimpleName());
            }
        });
    }
}
