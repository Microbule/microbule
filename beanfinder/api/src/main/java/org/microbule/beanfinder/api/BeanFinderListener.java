package org.microbule.beanfinder.api;

public interface BeanFinderListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    boolean beanFound(B bean);

    void beanLost(B bean);
}
