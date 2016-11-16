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
                .addFeature(Features.LOGGING_FEATURE_NAME, (cfg) -> Features.createLoggingFeature())
                .addFeature(Features.SWAGGER_FEATURE_NAME, (cfg) -> Features.createSwaggerFeature())
                .addFeature(Features.GZIP_FEATURE_NAME, Features::createGzipFeature)
                .build());
        sf.setProviders(config.getProviders());
        return new JaxrsServerImpl(sf.create());
    }
}
