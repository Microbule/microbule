package org.microbule.decorator.cors;

import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;

public class CorsDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server) {
        server.addProvider(new CorsFilter(server));
    }
}
