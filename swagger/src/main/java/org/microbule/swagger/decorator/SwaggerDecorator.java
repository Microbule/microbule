package org.microbule.swagger.decorator;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("swaggerDecorator")
public class SwaggerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setPrettyPrint(true);
        descriptor.addFeature(feature);
    }

    @Override
    public String name() {
        return "swagger";
    }
}
