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
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.microbule.cache.annotation.Cacheable;
import org.microbule.util.jaxrs.AnnotationDrivenDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class CacheDynamicFeature extends AnnotationDrivenDynamicFeature<Cacheable> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDynamicFeature.class);

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void configure(Cacheable annotation, ResourceInfo resourceInfo, FeatureContext featureContext) {
        final Method method = resourceInfo.getResourceMethod();
        getAnnotation(method, GET.class).ifPresent(get -> {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Adding cache filter to method {}({})...", resourceInfo.getResourceMethod().getName(), Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(Collectors.joining(", ")));
            }
            featureContext.register(new ContainerCacheFilter(annotation));
        });
    }
}
