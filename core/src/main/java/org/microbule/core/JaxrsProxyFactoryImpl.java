package org.microbule.core;

import java.util.List;
import java.util.Map;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

import static org.microbule.core.Features.GZIP_FEATURE_NAME;
import static org.microbule.core.Features.LOGGING_FEATURE_NAME;

public class JaxrsProxyFactoryImpl extends JaxrsObjectDecoratorRegistry<JaxrsProxyConfig, JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, String baseAddress, Map<String, Object> properties) {
        //Message m = JAXRSUtils.getCurrentMessage(); (HttpConduit)m.getExchange().get(ConduitSelector.class).selectConduit(m)
        //message.put(HTTPClientPolicy.class, myCustomPolicy)
        final JaxrsProxyConfigImpl jaxrsProxy = new JaxrsProxyConfigImpl(serviceInterface, baseAddress, properties);
        decorate(jaxrsProxy);
        final List<Feature> features = new FeaturesBuilder<>(jaxrsProxy)
                .addFeature(LOGGING_FEATURE_NAME, (cfg) -> Features.createLoggingFeature())
                .addFeature(GZIP_FEATURE_NAME, Features::createGzipFeature)
                .build();
        final JAXRSClientFactoryBean factoryBean = new JAXRSClientFactoryBean();
        factoryBean.setFeatures(features);
        factoryBean.setThreadSafe(true);
        factoryBean.setSecondsToKeepState(60);
        factoryBean.setProviders(jaxrsProxy.getProviders());
        factoryBean.setServiceClass(serviceInterface);
        factoryBean.setAddress(baseAddress);
        return factoryBean.create(serviceInterface);
    }
}
