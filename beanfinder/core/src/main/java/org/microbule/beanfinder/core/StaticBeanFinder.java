package org.microbule.beanfinder.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.microbule.beanfinder.api.BeanFinderListener;

public abstract class StaticBeanFinder extends AbstractBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<FinderRegistration<?>> registrations = new CopyOnWriteArrayList<>();
    private final AtomicBoolean initialized = new AtomicBoolean(false);

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract <B> Iterable<B> beansOfType(Class<B> beanType);

//----------------------------------------------------------------------------------------------------------------------
// BeanFinder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> void findBeans(Class<B> beanType, BeanFinderListener<B> listener) {
        final FinderRegistration<B> reg = new FinderRegistration<>(beanType, listener);
        registrations.add(reg);
        if(initialized.get()) {
            beanFound(reg);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <B> void beanFound(FinderRegistration<B> registration) {
        beansOfType(registration.getBeanType()).forEach(bean -> registration.getListener().beanFound(bean));
    }

    @SuppressWarnings("unchecked")
    protected <B> void beanRemoved(B bean) {
        registrations.stream()
                .filter(reg -> reg.getBeanType().isInstance(bean))
                .map(reg -> (FinderRegistration<B>)reg)
                .forEach(reg -> reg.getListener().beanLost(bean));
    }

    public void initialize() {
        initialized.set(true);
        registrations.forEach(this::beanFound);
        complete();
    }
}
