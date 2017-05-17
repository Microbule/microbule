package org.microbule.core;

import org.microbule.spi.JaxrsAddressChooser;

public class JaxrsProxyDispatcher<T> implements AutoCloseable {
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
// AutoCloseable Implementation
//----------------------------------------------------------------------------------------------------------------------

    public void close() {
        addressChooser.close();
        targetCache.close();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public T getTarget() {
        return targetCache.getTarget(addressChooser.chooseAddress());
    }
}
