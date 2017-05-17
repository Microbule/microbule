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

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.microbule.spi.JaxrsDynamicProxyStrategy;
import org.microbule.util.proxy.DynamicProxyUtils;
import org.microbule.util.reflect.ClassLoaderResolver;

public class JdkDynamicProxyStrategy implements JaxrsDynamicProxyStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<ClassLoaderResolver> resolverRef;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JdkDynamicProxyStrategy(AtomicReference<ClassLoaderResolver> resolverRef) {
        this.resolverRef = resolverRef;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsDynamicProxyStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public <T> T createDynamicProxy(Class<T> type, Supplier<T> targetSupplier, String descriptionPattern, Object... descriptionParams) {
        return DynamicProxyUtils.createProxy(resolverRef.get().resolveClassLoader(type, getClass().getClassLoader()), type, targetSupplier, descriptionPattern, descriptionParams);
    }
}
