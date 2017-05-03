package org.microbule.core;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.common.collect.MapMaker;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.beanfinder.api.BeanFinderListener;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServiceDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.slf4j.Logger;

public abstract class JaxrsServiceDecoratorRegistry<T extends JaxrsServiceDecorator> implements BeanFinderListener<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENABLED_PROPERTY = "enabled";
    private final Map<String, T> decoratorsMap = new MapMaker().makeMap();
    private final Class<T> decoratorType;
    private final BeanFinder finder;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDecoratorRegistry(Class<T> decoratorType, BeanFinder finder) {
        this.decoratorType = decoratorType;
        this.finder = finder;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Logger getLogger();

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(T bean) {
        if (decoratorsMap.putIfAbsent(bean.name(), bean) == null) {
            getLogger().info("Registered {} named \"{}\".", decoratorType.getSimpleName(), bean.name());
            return true;
        }
        return false;
    }

    @Override
    public void beanLost(T bean) {
        decoratorsMap.remove(bean.name());
    }

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

    public T getDecorator(String name) {
        return decoratorsMap.get(name);
    }

    public int getDecoratorCount() {
        return decoratorsMap.size();
    }

    @PostConstruct
    public void start() {
        finder.findBeans(decoratorType, this);
    }
}
