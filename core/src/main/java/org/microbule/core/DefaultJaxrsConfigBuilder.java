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

import java.util.LinkedList;
import java.util.List;

import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.CompositeConfig;
import org.microbule.spi.JaxrsConfigBuilder;

public class DefaultJaxrsConfigBuilder<T> implements JaxrsConfigBuilder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ConfigService configService;
    private final Class<T> serviceInterface;
    private final String serviceName;
    private final List<Config> members = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultJaxrsConfigBuilder(ConfigService configService, Class<T> serviceInterface, String serviceName) {
        this.configService = configService;
        this.serviceInterface = serviceInterface;
        this.serviceName = serviceName;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsConfigBuilder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config build() {
        return CompositeConfig.create(members);
    }

    @Override
    public Class<T> serviceInterface() {
        return serviceInterface;
    }

    @Override
    public String serviceName() {
        return serviceName;
    }

    @Override
    public JaxrsConfigBuilder withPath(String... path) {
        members.add(configService.createConfig(path));
        return this;
    }
}
