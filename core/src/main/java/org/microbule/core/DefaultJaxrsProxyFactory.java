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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsConfigService;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.scheduler.api.SchedulerService;
import org.microbule.spi.JaxrsAddressChooser;
import org.microbule.spi.JaxrsDynamicProxyStrategy;
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
    private final SchedulerService schedulerService;
    private final Supplier<LoadingCache<Class<?>, JaxrsProxyDispatcher<? extends Object>>> proxyCacheSupplier;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsProxyFactory(MicrobuleContainer container, JaxrsConfigService configService, SchedulerService schedulerService) {
        super(JaxrsProxyDecorator.class, container);
        this.configService = configService;
        this.schedulerService = schedulerService;
        this.serviceDiscovery = container.pluginReference(JaxrsServiceDiscovery.class, new DefaultJaxrsServiceDiscovery(configService));
        this.dynamicProxyStrategy = container.pluginReference(JaxrsDynamicProxyStrategy.class, new JdkDynamicProxyStrategy());
        this.namingStrategy = container.pluginReference(JaxrsServiceNamingStrategy.class, new DefaultJaxrsServiceNamingStrategy());
        this.proxyCacheSupplier = Suppliers.memoize(this::createProxyCache);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsProxyFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createProxy(Class<T> serviceInterface) {
        return dynamicProxyStrategy.get().createDynamicProxy(serviceInterface, () -> serviceInterface.cast(proxyCacheSupplier.get().getUnchecked(serviceInterface).getTarget()), "Microbule \"%s\" proxy", serviceInterface.getSimpleName());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private LoadingCache<Class<?>, JaxrsProxyDispatcher<?>> createProxyCache() {
        final Config config = configService.createConfig(JaxrsProxyFactory.class.getSimpleName(), "cache");
        final Long expiration = config.longValue("expiration").orElse(5L);
        final TimeUnit expirationUnit = config.enumValue("expirationUnit", TimeUnit.class).orElse(TimeUnit.MINUTES);
        final Long cleanupPeriod = config.longValue("cleanupPeriod").orElse(10L);
        final TimeUnit cleanupPeriodUnit = config.enumValue("cleanupPeriodUnit", TimeUnit.class).orElse(TimeUnit.MINUTES);

        final LoadingCache<Class<?>, JaxrsProxyDispatcher<? extends Object>> cache = CacheBuilder.newBuilder().expireAfterAccess(expiration, expirationUnit).build(new CacheLoader<Class<?>, JaxrsProxyDispatcher<? extends Object>>() {
            @Override
            public JaxrsProxyDispatcher<? extends Object> load(Class<?> serviceInterface) throws Exception {
                return createDispatcher(serviceInterface);
            }
        });
        schedulerService.schedule(() -> {
            LOGGER.debug("Cleaning up JAX-RS service proxy cache...");
            cache.cleanUp();
            LOGGER.debug("Finished cleaning up JAX-RS service proxy cache.");
        }, cleanupPeriod, cleanupPeriodUnit);
        return cache;
    }

    private <T> JaxrsProxyDispatcher<T> createDispatcher(Class<T> serviceInterface) {
        final String serviceName = namingStrategy.get().serviceName(serviceInterface);
        LOGGER.info("Creating {} JAX-RS proxy dispatcher ({})...", serviceName, serviceInterface.getSimpleName());
        final JaxrsTargetCache<T> proxyCache = createProxyCache(serviceInterface, serviceName);
        final JaxrsAddressChooser addressChooser = createEndpointChooser(serviceInterface, serviceName);
        return new JaxrsProxyDispatcher<>(proxyCache, addressChooser);
    }

    private <T> JaxrsTargetCache<T> createProxyCache(Class<T> serviceInterface, String serviceName) {
        final Config cacheConfig = configService.createConfig(JaxrsProxyFactory.class.getSimpleName(), "cache");
        LOGGER.debug("Creating JaxrsTargetCache<{}> for \"{}\" service.", serviceInterface.getSimpleName(), serviceName);
        return new JaxrsTargetCache<>(baseAddress -> {
            LOGGER.debug("Creating dynamic proxy for \"{}\" service at address: {}", serviceName, baseAddress);
            final Config config = configService.createProxyConfig(serviceInterface, serviceName);
            final DefaultJaxrsServiceDescriptor descriptor = new DefaultJaxrsServiceDescriptor(serviceInterface);
            decorate(descriptor, config);
            return JAXRSClientFactory.create(baseAddress, serviceInterface, descriptor.getProviders(), descriptor.getFeatures(), null);
        }, schedulerService, cacheConfig);
    }

    private <T> JaxrsAddressChooser createEndpointChooser(Class<T> serviceInterface, String serviceName) {
        LOGGER.debug("Creating \"{}\" service JaxrsAddressChooser ({})...", serviceName, serviceInterface.getSimpleName());
        return serviceDiscovery.get().createEndpointChooser(serviceInterface, serviceName);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
