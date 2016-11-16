package org.microbule.core;

import java.util.Map;

import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

public class JaxrsServerFactoryImpl extends JaxrsObjectDecoratorRegistry<JaxrsServerConfig, JaxrsServerDecorator> implements JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String LOGGING_FEATURE_NAME = "logging";
    public static final String SWAGGER_FEATURE_NAME = "swagger";
    public static final String GZIP_FEATURE_NAME = "gzip";

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, String baseAddress, Map<String, Object> properties) {
        final JaxrsServerConfigImpl config = new JaxrsServerConfigImpl(serviceInterface, baseAddress, properties);
        decorate(config);
        final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setBus(BusFactory.getDefaultBus(true));
        sf.setServiceBean(serviceImplementation);
        sf.setAddress(baseAddress);
        sf.setFeatures(new FeaturesBuilder<>(config)
                .addFeature(LOGGING_FEATURE_NAME, Features::createLoggingFeature)
                .addFeature(SWAGGER_FEATURE_NAME, Features::createSwaggerFeature)
                .addFeature(GZIP_FEATURE_NAME, Features::createGzipFeature)
                .build());
        sf.setProviders(config.getProviders());
        return new JaxrsServerImpl(sf.create());
    }
}
