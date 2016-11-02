package org.microbule.core;

import java.util.Map;

import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.spi.JaxrsProxyDecorator;
import org.osgi.framework.BundleContext;

import static java.util.Collections.singletonList;

public class JaxrsProxyFactoryImpl extends JaxrsDecoratorWhiteboard<JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsProxyFactoryImpl(BundleContext bundleContext) {
        super(bundleContext, JaxrsProxyDecorator.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, String baseAddress, Map<String, String> properties) {
        final JaxrsProxyImpl jaxrsProxy = new JaxrsProxyImpl(serviceInterface, properties);
        decoratorsFor(jaxrsProxy).forEach(decorator -> decorator.decorate(jaxrsProxy));
        return JAXRSClientFactory.create(baseAddress, serviceInterface, jaxrsProxy.getProviders(), singletonList(new LoggingFeature()), null);
    }
}
