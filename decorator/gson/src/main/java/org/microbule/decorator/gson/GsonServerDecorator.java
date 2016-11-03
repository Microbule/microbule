package org.microbule.decorator.gson;

import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class GsonServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonFactory factory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonServerDecorator(GsonFactory factory) {
        this.factory = factory;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsObjectDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new GsonProvider(factory));
    }
}