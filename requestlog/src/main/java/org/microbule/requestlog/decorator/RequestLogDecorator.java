package org.microbule.requestlog.decorator;

import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named
public class RequestLogDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void decorate(JaxrsServiceDescriptor server, Config config) {
        server.addProvider(new RequestLogFilter());
    }

    @Override
    public String name() {
        return "requestlog";
    }
}
