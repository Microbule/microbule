package org.microbule.core;

import java.util.List;
import java.util.Map;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

import static org.microbule.core.JaxrsServerFactoryImpl.GZIP_FEATURE_NAME;
import static org.microbule.core.JaxrsServerFactoryImpl.LOGGING_FEATURE_NAME;

public class JaxrsProxyFactoryImpl extends JaxrsObjectDecoratorRegistry<JaxrsProxyConfig, JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, String baseAddress, Map<String, Object> properties) {
        final JaxrsProxyConfigImpl jaxrsProxy = new JaxrsProxyConfigImpl(serviceInterface, baseAddress, properties);
        decorate(jaxrsProxy);
        final List<Feature> features = new FeaturesBuilder<>(jaxrsProxy)
                .addFeature(LOGGING_FEATURE_NAME, Features::createLoggingFeature)
                .addFeature(GZIP_FEATURE_NAME, Features::createGzipFeature)
                .build();
        return JAXRSClientFactory.create(baseAddress, serviceInterface, jaxrsProxy.getProviders(), features, null);
    }
}
