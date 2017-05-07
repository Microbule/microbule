package org.microbule.config.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigBuilder;
import org.microbule.config.api.ConfigBuilderFactory;
import org.microbule.config.spi.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("configService")
public class DefaultConfigBuilderFactory implements ConfigBuilderFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigBuilderFactory.class);

    private final Set<ConfigProvider> providers;
    private final BeanFinder finder;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultConfigBuilderFactory(BeanFinder beanFinder) {
        this.finder = beanFinder;
        this.providers = beanFinder.beanSortedSet(ConfigProvider.class, Comparator.comparingInt(ConfigProvider::priority));
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigBuilderFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ConfigBuilder createBuilder() {
        finder.awaitCompletion();
        return new ConfigBuilderImpl();
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private class ConfigBuilderImpl implements ConfigBuilder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private List<String[]> paths = new LinkedList<>();
        private Config custom = EmptyConfig.INSTANCE;

//----------------------------------------------------------------------------------------------------------------------
// ConfigBuilder Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Config build() {
            LOGGER.info("Collecting configurations from {}...", providers.stream().map(ConfigProvider::name).collect(Collectors.joining(", ")));
            List<Config> members = new LinkedList<>();
            collectConfigs(members, provider -> provider.priority() < 0);
            members.add(custom);
            collectConfigs(members, provider -> provider.priority() >= 0);
            return new CompositeConfig(members);
        }

        @Override
        public ConfigBuilder withCustom(Config custom) {
            this.custom = custom;
            return this;
        }

        @Override
        public ConfigBuilder withPath(String... path) {
            paths.add(path);
            return this;
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        private void collectConfigs(List<Config> members, Predicate<ConfigProvider> predicate) {
            providers.stream().filter(predicate).flatMap(provider -> providerConfigs(provider, paths).stream()).forEach(members::add);
        }

        private List<Config> providerConfigs(ConfigProvider provider, List<String[]> paths) {
            return paths.stream().map(provider::getConfig).collect(Collectors.toList());
        }
    }
}
