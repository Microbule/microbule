package org.microbule.decorator.requestlog;

import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServerProperties;

public class RequestLogDecorator implements JaxrsServerDecorator {
    @Override
    public void decorate(JaxrsServer server, JaxrsServerProperties properties) {
        server.addProvider(new RequestLogFilter());
    }
}
