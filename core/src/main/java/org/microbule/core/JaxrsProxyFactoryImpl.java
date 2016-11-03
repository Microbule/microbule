package org.microbule.core;

import java.util.Map;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyConfig;
import org.microbule.spi.JaxrsProxyDecorator;

import static java.util.Collections.singletonList;

public class JaxrsProxyFactoryImpl extends JaxrsObjectDecoratorRegistry<JaxrsProxyConfig,JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, String baseAddress, Map<String, Object> properties) {
        final JaxrsProxyConfigImpl jaxrsProxy = new JaxrsProxyConfigImpl(serviceInterface, baseAddress, properties);
        decorate(jaxrsProxy);
        return JAXRSClientFactory.create(baseAddress, serviceInterface, jaxrsProxy.getProviders(), singletonList(new LoggingFeature()), null);
    }
}
