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
    public <T> Config createProxyConfig(Class<T> serviceInterface) {
        return serviceConfigBuilder.get().buildProxyConfig(serviceInterface, configBuilderFactory.createBuilder()).build();
    }

    @Override
    public <T> Config createServerConfig(Class<T> serviceInterface) {
        return serviceConfigBuilder.get().buildServerConfig(serviceInterface, configBuilderFactory.createBuilder()).build();
    }
}
