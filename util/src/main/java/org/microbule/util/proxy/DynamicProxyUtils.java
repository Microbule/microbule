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

package org.microbule.util.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Supplier;

public final class DynamicProxyUtils {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String TO_STRING_METHOD_NAME = "toString";
    private static final String EQUALS_METHOD_NAME = "equals";
    private static final String HASH_CODE_METHOD_NAME = "hashCode";

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static <T> T createProxy(final Class<T> proxyInterface, InvocationHandler handler) {
        return proxyInterface.cast(Proxy.newProxyInstance(proxyInterface.getClassLoader(), new Class[]{proxyInterface}, handler));
    }

    public static <T> T createProxy(final Class<T> proxyInterface, final Supplier<T> targetSupplier, String descriptionPattern, Object... descriptionParams) {
        final String description = String.format(descriptionPattern, descriptionParams);
        final InvocationHandler handler = (proxy, method, args) -> {
            if (isEqualsMethod(method)) {
                return proxy == args[0];
            }
            if (isHashCode(method)) {
                return System.identityHashCode(proxy);
            }
            if (isToStringMethod(method)) {
                return description;
            }
            try {
                return method.invoke(targetSupplier.get(), args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        };
        return createProxy(proxyInterface, handler);
    }

    public static boolean isEqualsMethod(Method method) {
        return EQUALS_METHOD_NAME.equals(method.getName()) &&
                method.getParameterTypes().length == 1
                && Object.class.equals(method.getParameterTypes()[0]);
    }

    public static boolean isHashCode(Method method) {
        return HASH_CODE_METHOD_NAME.equals(method.getName()) && method.getParameterTypes().length == 0;
    }

    public static boolean isToStringMethod(Method method) {
        return TO_STRING_METHOD_NAME.equals(method.getName()) && method.getParameterCount() == 0;
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private DynamicProxyUtils() {

    }
}
