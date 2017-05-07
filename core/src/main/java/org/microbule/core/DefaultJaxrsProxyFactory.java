package org.microbule.core;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigurationException;
import org.microbule.spi.JaxrsProxyDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("jaxrsProxyFactory")
public class DefaultJaxrsProxyFactory extends JaxrsServiceDecoratorRegistry<JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxrsProxyFactory.class);

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsProxyFactory(BeanFinder finder) {
        super(JaxrsProxyDecorator.class, finder);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface, Config config) {
        getFinder().awaitCompletion();
        final String baseAddress = config.value(ADDRESS_PROP).orElseThrow(() -> new ConfigurationException("Missing '%s' property.", ADDRESS_PROP));
        final JaxrsServiceDescriptorImpl descriptor = new JaxrsServiceDescriptorImpl(serviceInterface);
        decorate(descriptor, config);
        return JAXRSClientFactory.create(baseAddress, serviceInterface, descriptor.getProviders(), descriptor.getFeatures(), null);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
