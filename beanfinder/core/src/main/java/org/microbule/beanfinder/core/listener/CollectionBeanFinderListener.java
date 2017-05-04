package org.microbule.beanfinder.core.listener;

import java.util.Collection;

import org.microbule.beanfinder.api.BeanFinderListener;

public class CollectionBeanFinderListener<B> implements BeanFinderListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Collection<B> collection;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CollectionBeanFinderListener(Collection<B> collection) {
        this.collection = collection;
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(B bean) {
        return collection.add(bean);
    }

    @Override
    public void beanLost(B bean) {
        collection.remove(bean);
    }
}
