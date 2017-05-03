package org.microbule.beanfinder.core;

import org.microbule.beanfinder.api.BeanFinderListener;

public class FinderRegistration<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<B> beanType;
    private final BeanFinderListener<B> listener;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public FinderRegistration(Class<B> beanType, BeanFinderListener<B> listener) {
        this.beanType = beanType;
        this.listener = listener;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public Class<B> getBeanType() {
        return beanType;
    }

    public BeanFinderListener<B> getListener() {
        return listener;
    }
}
