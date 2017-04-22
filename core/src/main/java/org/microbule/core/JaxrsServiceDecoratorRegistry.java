package org.microbule.core;

import java.util.Map;

import com.google.common.collect.MapMaker;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServiceDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JaxrsServiceDecoratorRegistry<T extends JaxrsServiceDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServiceDecoratorRegistry.class);

    private static final String ENABLED_PROPERTY = "enabled";
    private final Map<String, T> decoratorsMap = new MapMaker().makeMap();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public boolean addDecorator(String name, T decorator) {
        return decoratorsMap.putIfAbsent(name, decorator) == null;
    }

    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        decoratorsMap.forEach((name, decorator) -> {
            final Config decoratorConfig = config.group(name);
            if (decoratorConfig.booleanValue(ENABLED_PROPERTY).orElse(true)) {
                LOGGER.info("Decorating {} service using decorator '{}'.", descriptor.serviceInterface().getSimpleName(), name);
                decorator.decorate(descriptor, decoratorConfig);
            } else {
                LOGGER.info("Skipping decorator '{}' for service {}.", name, descriptor.serviceInterface().getSimpleName());
            }
        });
    }

    public T getDecorator(String name) {
        return decoratorsMap.get(name);
    }

    public int getDecoratorCount() {
        return decoratorsMap.size();
    }

    public void removeDecorator(String name) {
        decoratorsMap.remove(name);
    }
}
