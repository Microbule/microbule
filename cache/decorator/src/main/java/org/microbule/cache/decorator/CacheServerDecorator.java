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
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.microbule.cache.annotation.Cacheable;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("cacheServerDecorator")
public class CacheServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor object, Config config) {
        final Set<Method> methods = MethodUtils.getMethodsListWithAnnotation(object.serviceInterface(), Cacheable.class).stream().filter(method -> method.isAnnotationPresent(GET.class)).collect(Collectors.toSet());
        if (!methods.isEmpty()) {
            object.addProvider(new CacheDynamicFeature(object.serviceInterface(), methods));
        }
        object.addProvider(new CacheInfoProvider());
    }

    @Override
    public String name() {
        return "cache";
    }
}
