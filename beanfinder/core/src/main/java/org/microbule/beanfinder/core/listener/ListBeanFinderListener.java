package org.microbule.beanfinder.core.listener;

import java.util.List;

import org.microbule.beanfinder.api.BeanFinderListener;

public class ListBeanFinderListener<B> implements BeanFinderListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<B> list;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ListBeanFinderListener(List<B> list) {
        this.list = list;
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(B bean) {
        return list.add(bean);
    }

    @Override
    public void beanLost(B bean) {
        list.remove(bean);
    }
}
