package org.microbule.beanfinder.core.listener;

import java.util.concurrent.atomic.AtomicReference;

import org.microbule.beanfinder.api.BeanFinderListener;

public class RefBeanFinderListener<B> implements BeanFinderListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<B> ref;
    private final B defaultValue;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RefBeanFinderListener(AtomicReference<B> ref, B defaultValue) {
        this.ref = ref;
        this.defaultValue = defaultValue;
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(B bean) {
        return ref.compareAndSet(defaultValue, bean);
    }

    @Override
    public void beanLost(B bean) {
        ref.compareAndSet(bean, defaultValue);
    }
}
