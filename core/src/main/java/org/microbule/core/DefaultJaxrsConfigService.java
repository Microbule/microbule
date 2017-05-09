package org.microbule.core;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.api.JaxrsConfigService;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigBuilderFactory;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsConfigBuilderStrategy;

@Singleton
@Named("jaxrsConfigService")
public class DefaultJaxrsConfigService implements JaxrsConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<JaxrsConfigBuilderStrategy> serviceConfigBuilder;
    private final ConfigBuilderFactory configBuilderFactory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJaxrsConfigService(MicrobuleContainer container, ConfigBuilderFactory configBuilderFactory) {
        this.configBuilderFactory = configBuilderFactory;
        serviceConfigBuilder = container.pluginReference(JaxrsConfigBuilderStrategy.class, new DefaultJaxrsConfigBuilderStrategy());
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsConfigService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> Config createProxyConfig(Class<T> serviceInterface, Config custom) {
        return serviceConfigBuilder.get().buildProxyConfig(serviceInterface, configBuilderFactory.createBuilder().withCustom(custom)).build();
    }

    @Override
    public <T> Config createServerConfig(Class<T> serviceInterface, Config custom) {
        return serviceConfigBuilder.get().buildServerConfig(serviceInterface, configBuilderFactory.createBuilder().withCustom(custom)).build();
    }
}
