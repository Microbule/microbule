/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.core;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.microbule.api.JaxrsConfigService;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigurationException;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("jaxrsServerFactory")
public class DefaultJaxrsServerFactory extends JaxrsServiceDecoratorRegistry<JaxrsServerDecorator> implements JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxrsServerFactory.class);

    private final JaxrsConfigService configService;
    private final AtomicReference<JaxrsServiceNamingStrategy> namingStrategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsServerFactory(MicrobuleContainer container, JaxrsConfigService configService) {
        super(JaxrsServerDecorator.class, container);
        this.configService = configService;
        this.namingStrategy = container.pluginReference(JaxrsServiceNamingStrategy.class, new DefaultJaxrsServiceNamingStrategy());
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation) {
        final Config config = configService.createServerConfig(serviceInterface, namingStrategy.get().serviceName(serviceInterface));
        final String address = config.value(ADDRESS_PROP).orElseThrow(() -> new ConfigurationException("Missing '%s' property.", ADDRESS_PROP));
        LOGGER.info("Starting {} JAX-RS server ({})...", serviceInterface.getSimpleName(), address);
        final JaxrsServiceDescriptorImpl descriptor = new JaxrsServiceDescriptorImpl(serviceInterface);
        decorate(descriptor, config);
        final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setBus(BusFactory.getDefaultBus(true));
        sf.setServiceBean(serviceImplementation);
        sf.setAddress(address);
        sf.setFeatures(descriptor.getFeatures());
        sf.setProviders(descriptor.getProviders());
        final Server server = sf.create();
        LOGGER.info("Successfully started {} JAX-RS server ({}).", serviceInterface.getSimpleName(), address);
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
