package org.microbule.core;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigurationException;
import org.microbule.spi.JaxrsServerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("jaxrsServerFactory")
public class DefaultJaxrsServerFactory extends JaxrsServiceDecoratorRegistry<JaxrsServerDecorator> implements JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxrsServerFactory.class);

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsServerFactory(BeanFinder finder) {
        super(JaxrsServerDecorator.class, finder);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, Config config) {
        getFinder().awaitCompletion();
        final String address = config.value(ADDRESS_PROP).orElseThrow(() -> new ConfigurationException("Missing '%s' property.", ADDRESS_PROP));
        final JaxrsServiceDescriptorImpl descriptor = new JaxrsServiceDescriptorImpl(serviceInterface);
        decorate(descriptor, config);
        final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setBus(BusFactory.getDefaultBus(true));
        sf.setServiceBean(serviceImplementation);
        sf.setAddress(address);
        sf.setFeatures(descriptor.getFeatures());
        sf.setProviders(descriptor.getProviders());
        final Server server = sf.create();
        return server::destroy;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
