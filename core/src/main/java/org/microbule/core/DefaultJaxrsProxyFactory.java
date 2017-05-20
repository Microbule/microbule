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

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.microbule.api.JaxrsConfigService;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.config.api.Config;
import org.microbule.config.core.RecordingConfig;
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
public class DefaultJaxrsProxyFactory extends JaxrsServiceDecoratorRegistry<JaxrsProxyDecorator> implements JaxrsProxyFactory, RemovalListener<Class<?>, JaxrsProxyDispatcher<? extends Object>> {
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
// RemovalListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void onRemoval(RemovalNotification<Class<?>, JaxrsProxyDispatcher<?>> notification) {
        notification.getValue().close();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private LoadingCache<Class<?>, JaxrsProxyDispatcher<?>> createProxyCache() {
        final Config config = configService.createConfig(JaxrsProxyFactory.class.getSimpleName(), "cache");
        final Long expiration = config.longValue("expiration").orElse(5L);
        final TimeUnit expirationUnit = config.enumValue("expirationUnit", TimeUnit.class).orElse(TimeUnit.MINUTES);
        return CacheBuilder.newBuilder()
                .removalListener(this)
                .expireAfterAccess(expiration, expirationUnit).build(new CacheLoader<Class<?>, JaxrsProxyDispatcher<? extends Object>>() {
                    @Override
                    public JaxrsProxyDispatcher<? extends Object> load(Class<?> serviceInterface) throws Exception {
                        return createDispatcher(serviceInterface);
                    }
                });
    }

    private <T> JaxrsProxyDispatcher<T> createDispatcher(Class<T> serviceInterface) {
        final String serviceName = namingStrategy.get().serviceName(serviceInterface);
        LOGGER.info("Creating \"{}\" proxy dispatcher ({})...", serviceName, serviceInterface.getSimpleName());
        final JaxrsProxyCache<T> proxyCache = createProxyCache(serviceInterface, serviceName);
        final JaxrsAddressChooser addressChooser = createEndpointChooser(serviceInterface, serviceName);
        return new JaxrsProxyDispatcher<>(proxyCache, addressChooser);
    }

    private <T> JaxrsProxyCache<T> createProxyCache(Class<T> serviceInterface, String serviceName) {
        final Config cacheConfig = configService.createConfig(JaxrsProxyFactory.class.getSimpleName(), "cache");
        LOGGER.debug("Creating JaxrsProxyCache<{}> for \"{}\" service.", serviceInterface.getSimpleName(), serviceName);
        return new JaxrsProxyCache<>(baseAddress -> {
            final RecordingConfig recordingConfig = new RecordingConfig(configService.createProxyConfig(serviceInterface, serviceName));
            final DefaultJaxrsServiceDescriptor descriptor = new DefaultJaxrsServiceDescriptor(serviceInterface, serviceName);
            decorate(descriptor, recordingConfig);
            final T proxy = JAXRSClientFactory.create(baseAddress, serviceInterface, descriptor.getProviders(), descriptor.getFeatures(), null);
            return new CachedJaxrsProxy<>(proxy, recordingConfig.getRecordedJson());
        }, schedulerService, serviceName, cacheConfig);
    }

    private <T> JaxrsAddressChooser createEndpointChooser(Class<T> serviceInterface, String serviceName) {
        LOGGER.debug("Creating \"{}\" service JaxrsAddressChooser ({})...", serviceName, serviceInterface.getSimpleName());
        return serviceDiscovery.get().createAddressChooser(serviceInterface, serviceName);
    }

    @PreDestroy
    public void destroy() {
        proxyCacheSupplier.get().invalidateAll();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
