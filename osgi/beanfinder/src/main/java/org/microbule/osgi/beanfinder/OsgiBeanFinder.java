package org.microbule.osgi.beanfinder;

import org.microbule.beanfinder.api.BeanFinderListener;
import org.microbule.beanfinder.core.AbstractBeanFinder;
import org.osgi.framework.BundleContext;

public class OsgiBeanFinder extends AbstractBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final BundleContext bundleContext;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OsgiBeanFinder(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> void findBeans(Class<B> beanType, BeanFinderListener<B> listener) {
        new BeanFinderListenerWhiteboard<>(bundleContext, beanType, listener).start();
    }
}
