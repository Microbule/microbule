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

package org.microbule.errormap.decorator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.errormap.api.ErrorMapperUtils;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("errorMapperProxyDecorator")
public class ErrorMapperProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapperProxyDecorator.class);
    private static final String STRATEGY = "strategy";
    private final ErrorMapperService errorMapperService;
    private final String defaultStrategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public ErrorMapperProxyDecorator(ErrorMapperService errorMapperService) {
        this(errorMapperService, ErrorMapperUtils.DEFAULT_STRATEGY);
    }

    public ErrorMapperProxyDecorator(ErrorMapperService errorMapperService, String defaultStrategy) {
        this.errorMapperService = errorMapperService;
        this.defaultStrategy = defaultStrategy;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final String strategy = config.value(STRATEGY).orElse(defaultStrategy);
        LOGGER.debug("Using \"{}\" error response strategy for {} JAX-RS proxy.", strategy, descriptor.serviceInterface().getSimpleName());
        descriptor.addProvider(new ErrorMapperResponseExceptionMapper(errorMapperService, strategy));
    }

    @Override
    public String name() {
        return "errormap";
    }
}
