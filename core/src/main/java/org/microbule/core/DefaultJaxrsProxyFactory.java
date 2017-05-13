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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsConfigService;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsDynamicProxyStrategy;
import org.microbule.spi.JaxrsEndpointChooser;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDiscovery;
import org.microbule.spi.JaxrsServiceNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("jaxrsProxyFactory")
public class DefaultJaxrsProxyFactory extends JaxrsServiceDecoratorRegistry<JaxrsProxyDecorator> implements JaxrsProxyFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJaxrsProxyFactory.class);
    private final JaxrsConfigService configService;
    private final AtomicReference<JaxrsServiceDiscovery> serviceDiscovery;
    private final AtomicReference<JaxrsDynamicProxyStrategy> dynamicProxyStrategy;
    private final AtomicReference<JaxrsServiceNamingStrategy> namingStrategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsProxyFactory(MicrobuleContainer container, JaxrsConfigService configService) {
        super(JaxrsProxyDecorator.class, container);
        this.configService = configService;
        this.serviceDiscovery = container.pluginReference(JaxrsServiceDiscovery.class, new DefaultJaxrsServiceDiscovery(configService));
        this.dynamicProxyStrategy = container.pluginReference(JaxrsDynamicProxyStrategy.class, new JdkDynamicProxyStrategy());
        this.namingStrategy = container.pluginReference(JaxrsServiceNamingStrategy.class, new DefaultJaxrsServiceNamingStrategy());
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface) {
        LOGGER.info("Creating {} JAX-RS service proxy.", serviceInterface.getSimpleName());
        final String serviceName = namingStrategy.get().serviceName(serviceInterface);
        final JaxrsProxyCache<T> cache = new JaxrsProxyCache<>(baseAddress -> {
            final Config config = configService.createProxyConfig(serviceInterface, serviceName);
            final JaxrsServiceDescriptorImpl descriptor = new JaxrsServiceDescriptorImpl(serviceInterface);
            decorate(descriptor, config);
            return JAXRSClientFactory.create(baseAddress, serviceInterface, descriptor.getProviders(), descriptor.getFeatures(), null);
        });
        final Supplier<JaxrsEndpointChooser> endpointChooserSupplier = Suppliers.memoize(() -> createEndpointChooser(serviceInterface, serviceName));
        return dynamicProxyStrategy.get().createDynamicProxy(serviceInterface, () -> cache.getProxy(endpointChooserSupplier.get().chooseEndpoint()));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> JaxrsEndpointChooser createEndpointChooser(Class<T> serviceInterface, String serviceName) {
        LOGGER.info("Creating {} JaxrsEndpointChooser...", serviceInterface.getSimpleName());
        return serviceDiscovery.get().createEndpointChooser(serviceInterface, serviceName);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
