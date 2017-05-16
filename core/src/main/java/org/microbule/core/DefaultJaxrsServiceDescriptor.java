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

import org.apache.cxf.feature.Feature;
import org.microbule.spi.JaxrsServiceDescriptor;

public class DefaultJaxrsServiceDescriptor implements JaxrsServiceDescriptor {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final List<Object> providers = new LinkedList<>();
    private final List<Feature> features = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    DefaultJaxrsServiceDescriptor(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDescriptor Implementation
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDescriptor addFeature(Feature feature) {
        features.add(feature);
        return this;
    }

    public JaxrsServiceDescriptor addProvider(Object provider) {
        providers.add(provider);
        return this;
    }

    public Class<?> serviceInterface() {
        return serviceInterface;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    List<Feature> getFeatures() {
        return features;
    }

    List<Object> getProviders() {
        return providers;
    }
}
