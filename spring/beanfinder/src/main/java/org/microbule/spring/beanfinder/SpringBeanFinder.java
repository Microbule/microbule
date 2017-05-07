package org.microbule.spring.beanfinder;

import org.microbule.beanfinder.core.StaticBeanFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanFinder extends StaticBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBeanFinder.class);

    @Autowired
    private ApplicationContext applicationContext;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected <B> Iterable<B> beansOfType(Class<B> beanType) {
        return applicationContext.getBeansOfType(beanType).values();
    }

    @EventListener
    @Order(0)
    public void onContextRefreshed(ContextRefreshedEvent event) {
        LOGGER.info("Initializing SpringBeanFinder...");
        initialize();
    }
}
