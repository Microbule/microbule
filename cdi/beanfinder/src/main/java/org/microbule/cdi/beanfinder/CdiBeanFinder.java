package org.microbule.cdi.beanfinder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.collect.Lists;
import org.microbule.beanfinder.core.StaticBeanFinder;
import org.microbule.cdi.core.event.BeanFinderStarted;

@Singleton
@Named("cdiBeanFinder")
public class CdiBeanFinder extends StaticBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    private BeanManager beanManager;

    @Inject
    private Event<BeanFinderStarted> event;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected <B> Iterable<B> beansOfType(Class<B> beanType) {
        final Instance<B> beans = CDI.current().select(beanType);
        return Lists.newArrayList(beans.iterator());
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) Object init) {
        start();
        event.fire(new BeanFinderStarted());
    }
}
