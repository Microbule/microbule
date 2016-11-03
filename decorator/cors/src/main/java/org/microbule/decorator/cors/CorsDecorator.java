package org.microbule.decorator.cors;

import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class CorsDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new CorsFilter(server));
    }
}
