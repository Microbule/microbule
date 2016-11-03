package org.microbule.decorator.gson;

import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

public class GsonProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonFactory factory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonProxyDecorator(GsonFactory factory) {
        this.factory = factory;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsProxyConfig proxy) {
        proxy.addProvider(new GsonProvider(factory));
    }
}