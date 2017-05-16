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

import java.util.Map;

import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsServiceDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.slf4j.Logger;

public abstract class JaxrsServiceDecoratorRegistry<T extends JaxrsServiceDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENABLED_PROPERTY = "enabled";
    private final Map<String, T> decoratorsMap;
    private final Class<T> decoratorType;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDecoratorRegistry(Class<T> decoratorType, MicrobuleContainer container) {
        this.decoratorType = decoratorType;
        this.decoratorsMap = container.pluginMap(decoratorType, JaxrsServiceDecorator::name);
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Logger getLogger();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        decoratorsMap.forEach((name, decorator) -> {
            final Config decoratorConfig = config.filtered(name);
            if (decoratorConfig.booleanValue(ENABLED_PROPERTY).orElse(Boolean.TRUE)) {
                getLogger().debug("Decorating {} service using {} \"{}\".", descriptor.serviceInterface().getSimpleName(), decoratorType.getSimpleName(), name);
                decorator.decorate(descriptor, decoratorConfig);
            } else {
                getLogger().debug("Skipping decorator {} \"{}\" for service {}.", name, decoratorType.getSimpleName(), descriptor.serviceInterface().getSimpleName());
            }
        });
    }
}
