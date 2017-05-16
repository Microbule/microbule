package org.microbule.core;

import org.microbule.spi.JaxrsAddressChooser;

public class JaxrsProxyDispatcher<T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final JaxrsTargetCache<T> targetCache;
    private final JaxrsAddressChooser addressChooser;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyDispatcher(JaxrsTargetCache<T> targetCache, JaxrsAddressChooser addressChooser) {
        this.targetCache = targetCache;
        this.addressChooser = addressChooser;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public T getTarget() {
        return targetCache.getTarget(addressChooser.chooseAddress());
    }
}
