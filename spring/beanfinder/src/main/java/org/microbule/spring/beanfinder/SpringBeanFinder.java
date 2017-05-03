package org.microbule.spring.beanfinder;

import org.microbule.beanfinder.core.StaticBeanFinder;
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
        initialize();
    }
}
