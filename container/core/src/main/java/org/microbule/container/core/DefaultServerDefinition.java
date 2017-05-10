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

package org.microbule.container.core;

import org.microbule.container.api.ServerDefinition;

public class DefaultServerDefinition implements ServerDefinition {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String id;
    private final Class<?> serviceInterface;
    private final Object serviceImplementation;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultServerDefinition(String id, Class<?> serviceInterface, Object serviceImplementation) {
        this.id = id;
        this.serviceInterface = serviceInterface;
        this.serviceImplementation = serviceImplementation;
    }

//----------------------------------------------------------------------------------------------------------------------
// ServerDefinition Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String id() {
        return id;
    }

    @Override
    public Object serviceImplementation() {
        return serviceImplementation;
    }

    @Override
    public Class<?> serviceInterface() {
        return serviceInterface;
    }
}
