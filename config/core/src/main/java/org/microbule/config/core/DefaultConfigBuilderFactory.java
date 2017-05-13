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

package org.microbule.config.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigBuilder;
import org.microbule.config.api.ConfigBuilderFactory;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.container.api.MicrobuleContainer;
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

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultConfigBuilderFactory(MicrobuleContainer container) {
        this.providers = container.pluginSortedSet(ConfigProvider.class, Comparator.comparingInt(ConfigProvider::priority));
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigBuilderFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ConfigBuilder createBuilder() {
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

//----------------------------------------------------------------------------------------------------------------------
// ConfigBuilder Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Config build() {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Collecting configurations from {}...", providers.stream().map(ConfigProvider::name).collect(Collectors.joining(", ")));
            }
            return new CompositeConfig(providers.stream().flatMap(provider -> providerConfigs(provider, paths).stream()).collect(Collectors.toList()));
        }

        @Override
        public ConfigBuilder withPath(String... path) {
            paths.add(path);
            return this;
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        private List<Config> providerConfigs(ConfigProvider provider, List<String[]> paths) {
            return paths.stream().map(provider::getConfig).collect(Collectors.toList());
        }
    }
}
