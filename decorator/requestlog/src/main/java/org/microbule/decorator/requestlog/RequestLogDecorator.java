package org.microbule.decorator.requestlog;

import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class RequestLogDecorator implements JaxrsServerDecorator {
    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(new RequestLogFilter());
    }
}
