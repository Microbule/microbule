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

import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;

import org.microbule.tracer.spi.TracerIdStrategy;

public class UuidStrategy implements TracerIdStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final TracerIdStrategy INSTANCE = new UuidStrategy();

//----------------------------------------------------------------------------------------------------------------------
// TracerIdStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String generateRequestId(ContainerRequestContext request) {
        return uuid();
    }

    @Override
    public String generateTraceId(ContainerRequestContext request) {
        return uuid();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private String uuid() {
        return UUID.randomUUID().toString();
    }
}
