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

package org.microbule.tracer.decorator;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.tracer.spi.TracerIdStrategy;

@Singleton
@Named("tracerServerDecorator")
public class TracerServerDecorator extends AbstractTracerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------


    private final AtomicReference<TracerIdStrategy> idGeneratorRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public TracerServerDecorator(MicrobuleContainer container) {
        this.idGeneratorRef = container.pluginReference(TracerIdStrategy.class, UuidStrategy.INSTANCE);
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final String traceIdHeader = config.value(TRACE_ID_HEADER_PROP).orElse(DEFAULT_TRACE_ID_HEADER);
        final String requestIdHeader = config.value(REQUEST_ID_HEADER_PROP).orElse(DEFAULT_REQUEST_ID_HEADER);
        descriptor.addProvider(new TracerContainerFilter(idGeneratorRef, traceIdHeader, requestIdHeader));
    }
}
