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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.container.api.MicrobuleContainer;
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
    public DefaultConfigService(MicrobuleContainer container) {
        this.providers = container.pluginSortedSet(ConfigProvider.class, Comparator.comparingInt(ConfigProvider::priority));
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config createConfig(String... path) {
        return CompositeConfig.create(providers.stream().map(provider -> providerConfig(provider, path)).collect(Collectors.toList()));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Config providerConfig(ConfigProvider provider, String[] path) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating configuration using provider \"{}\" at path \"/{}\".", provider.name(), Stream.of(path).collect(Collectors.joining("/")));
        }
        return provider.getConfig(path);
    }
}
