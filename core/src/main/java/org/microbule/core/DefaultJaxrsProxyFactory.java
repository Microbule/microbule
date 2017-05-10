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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
import org.microbule.spi.JaxrsEndpointChooser;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDiscovery;
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

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsProxyFactory(MicrobuleContainer container, JaxrsConfigService configService) {
        super(JaxrsProxyDecorator.class, container);
        this.configService = configService;
        this.serviceDiscovery = container.pluginReference(JaxrsServiceDiscovery.class, new DefaultJaxrsServiceDiscovery(configService));
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface) {
        LOGGER.info("Creating {} JAX-RS service proxy.", serviceInterface.getSimpleName());
        final JaxrsProxyCache<T> cache = new JaxrsProxyCache<>((baseAddress) -> {
            final Config config = configService.createProxyConfig(serviceInterface);
            final JaxrsServiceDescriptorImpl descriptor = new JaxrsServiceDescriptorImpl(serviceInterface);
            decorate(descriptor, config);
            return JAXRSClientFactory.create(baseAddress, serviceInterface, descriptor.getProviders(), descriptor.getFeatures(), null);
        });
        final Supplier<JaxrsEndpointChooser> endpointChooserSupplier = Suppliers.memoize(() -> createEndpointChooser(serviceInterface));
        final JaxrsProxyInvocationHandler invocationHandler = new JaxrsProxyInvocationHandler<>(endpointChooserSupplier, cache);
        return serviceInterface.cast(Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, invocationHandler));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> JaxrsEndpointChooser createEndpointChooser(Class<T> serviceInterface) {
        LOGGER.info("Creating {} JaxrsEndpointChooser...", serviceInterface.getSimpleName());
        return serviceDiscovery.get().createEndpointChooser(serviceInterface);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static final class JaxrsProxyInvocationHandler<T> implements InvocationHandler {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final Supplier<JaxrsEndpointChooser> endpointChooserProvider;
        private final JaxrsProxyCache<T> proxyCache;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public JaxrsProxyInvocationHandler(Supplier<JaxrsEndpointChooser> endpointChooserProvider, JaxrsProxyCache<T> proxyCache) {
            this.endpointChooserProvider = endpointChooserProvider;
            this.proxyCache = proxyCache;
        }

//----------------------------------------------------------------------------------------------------------------------
// InvocationHandler Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final String endpoint = endpointChooserProvider.get().chooseEndpoint();
            final T delegate = proxyCache.getProxy(endpoint);
            try {
                return method.invoke(delegate, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
