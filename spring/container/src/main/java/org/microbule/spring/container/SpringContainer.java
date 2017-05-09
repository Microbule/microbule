package org.microbule.spring.container;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import javax.ws.rs.Path;

import org.microbule.container.api.ServerDefinition;
import org.microbule.container.core.DefaultServerDefinition;
import org.microbule.container.core.StaticContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringContainer extends StaticContainer implements BeanPostProcessor {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContainer.class);
    private final List<ServerDefinition> serverDefinitions = new CopyOnWriteArrayList<>();


    @Autowired
    private ApplicationContext applicationContext;

//----------------------------------------------------------------------------------------------------------------------
// BeanPostProcessor Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Stream.of(bean.getClass().getInterfaces()).forEach(serviceInterface -> {
            if(serviceInterface.isAnnotationPresent(Path.class)) {
                LOGGER.info("Discovered {} service implementation bean named \"{}\".", serviceInterface.getSimpleName(), beanName);
                serverDefinitions.add(new DefaultServerDefinition(beanName, serviceInterface, bean));
            }
        });
        return bean;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        initialize();
    }

    @Override
    protected <B> Iterable<B> plugins(Class<B> beanType) {
        return applicationContext.getBeansOfType(beanType).values();
    }

    @Override
    protected Iterable<ServerDefinition> servers() {
        return serverDefinitions;
    }
}
