package org.microbule.config.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.spi.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("configService")
public class DefaultConfigService implements ConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigService.class);

    private final Set<ConfigProvider> providers;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultConfigService(BeanFinder beanFinder) {
        this.providers = beanFinder.beanSortedSet(ConfigProvider.class, Comparator.comparingInt(ConfigProvider::priority));
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return getProxyConfig(serviceInterface, EmptyConfig.INSTANCE);
    }

    @Override
    public Config getProxyConfig(Class<?> serviceInterface, Config overrides) {
        return collectConfigs(overrides, provider -> provider.getProxyConfig(serviceInterface));
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return getServerConfig(serviceInterface, EmptyConfig.INSTANCE);
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface, Config overrides) {
        return collectConfigs(overrides, provider -> provider.getServerConfig(serviceInterface));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Config collectConfigs(Config overrides, Function<ConfigProvider, Config> fn) {
        LOGGER.info("Collecting configurations from {}...", providers.stream().map(ConfigProvider::name).collect(Collectors.joining(", ")));
        List<Config> members = new LinkedList<>();
        providers.stream().filter(provider -> provider.priority() < 0).map(fn).forEach(members::add);
        members.add(overrides);
        providers.stream().filter(provider -> provider.priority() >= 0).map(fn).forEach(members::add);
        return new CompositeConfig(members);
    }
}
