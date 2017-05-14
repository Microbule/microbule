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

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.microbule.annotation.JaxrsService;
import org.microbule.spi.JaxrsServiceNamingStrategy;

public class DefaultJaxrsServiceNamingStrategy implements JaxrsServiceNamingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceNamingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String serverAddress(Class<?> serviceInterface) {
        final String fallback = "/" + serviceName(serviceInterface);
        return annotationProperty(serviceInterface, fallback, annotation -> StringUtils.defaultIfBlank(annotation.serverAddress(), fallback));
    }

    @Override
    public String serviceName(Class<?> serviceInterface) {
        final String fallback = serviceInterface.getSimpleName();
        return annotationProperty(serviceInterface, fallback, annotation -> StringUtils.defaultIfBlank(annotation.name(), fallback));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> T annotationProperty(Class<?> serviceInterface, T fallback, Function<JaxrsService, T> fn) {
        return Optional.ofNullable(serviceInterface.getAnnotation(JaxrsService.class))
                .map(fn)
                .orElse(fallback);
    }
}
