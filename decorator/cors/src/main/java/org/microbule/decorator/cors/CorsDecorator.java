package org.microbule.decorator.cors;

import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServerProperties;

public class CorsDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server, JaxrsServerProperties properties) {
        server.addProvider(new CorsFilter(properties));
    }
}
