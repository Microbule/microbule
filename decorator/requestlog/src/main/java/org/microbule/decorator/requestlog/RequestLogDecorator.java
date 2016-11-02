package org.microbule.decorator.requestlog;

import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;

public class RequestLogDecorator implements JaxrsServerDecorator {
    @Override
    public void decorate(JaxrsServer server) {
        server.addProvider(new RequestLogFilter());
    }
}
