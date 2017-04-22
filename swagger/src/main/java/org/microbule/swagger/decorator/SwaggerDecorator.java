package org.microbule.swagger.decorator;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

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
}
