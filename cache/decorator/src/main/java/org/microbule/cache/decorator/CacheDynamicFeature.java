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

package org.microbule.cache.decorator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.microbule.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CacheDynamicFeature implements DynamicFeature {
    //----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDynamicFeature.class);

    private final Class<?> serviceInterface;
    private final Set<Method> methods;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CacheDynamicFeature(Class<?> serviceInterface, Set<Method> methods) {
        this.serviceInterface = serviceInterface;
        this.methods = methods;
    }

//----------------------------------------------------------------------------------------------------------------------
// DynamicFeature Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        try {
            final Method resourceMethod = serviceInterface.getMethod(resourceInfo.getResourceMethod().getName(), resourceInfo.getResourceMethod().getParameterTypes());
            if (methods.contains(resourceMethod)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Adding cache filter to method {}({})...", resourceMethod.getName(), Arrays.stream(resourceMethod.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
                }
                context.register(new ContainerCacheFilter(resourceMethod.getAnnotation(Cacheable.class)));
            }
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Method {}() is not found on service interface {}.", resourceInfo.getResourceMethod().getName(), serviceInterface.getCanonicalName());
        }
    }
}
