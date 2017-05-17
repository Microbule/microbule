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

package org.microbule.cglib;

import java.util.function.Supplier;

import javax.inject.Named;
import javax.inject.Singleton;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.microbule.spi.JaxrsDynamicProxyStrategy;
import org.microbule.util.proxy.DynamicProxyUtils;
import org.microbule.util.reflect.DelegatingClassLoader;

@Named("cglibDynamicProxyStrategy")
@Singleton
public class CglibDynamicProxyStrategy implements JaxrsDynamicProxyStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final CallbackFilter FILTER = method -> {
        if (DynamicProxyUtils.isEqualsMethod(method)) {
            return 1;
        } else if (DynamicProxyUtils.isHashCode(method)) {
            return 2;
        } else if (DynamicProxyUtils.isToStringMethod(method)) {
            return 3;
        } else {
            return 0;
        }
    };

//----------------------------------------------------------------------------------------------------------------------
// JaxrsDynamicProxyStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T createDynamicProxy(Class<T> type, Supplier<T> targetSupplier, String descriptionPattern, Object... descriptionParams) {
        final String description = String.format(descriptionPattern, descriptionParams);
        final Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(new DelegatingClassLoader(type.getClassLoader(), Enhancer.class.getClassLoader()));
        enhancer.setInterfaces(new Class[]{type});
        enhancer.setSuperclass(Object.class);
        enhancer.setCallbackFilter(FILTER);
        enhancer.setCallbacks(new Callback[]{
                (Dispatcher) targetSupplier::get,
                (MethodInterceptor) (proxy, method, params, methodProxy) -> proxy == params[0],
                (MethodInterceptor) (proxy, method, params, methodProxy) -> System.identityHashCode(proxy),
                (MethodInterceptor) (proxy, method, params, methodProxy) -> description
        });
        return type.cast(enhancer.create());
    }
}
