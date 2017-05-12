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

package org.microbule.config.api;

import java.util.Optional;
import java.util.function.Function;

public interface Config {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    default Optional<Boolean> booleanValue(String key) {
        return value(key, Boolean::valueOf);
    }

    Config filtered(String... paths);


    default Optional<Double> doubleValue(String key) {
        return value(key, Double::valueOf);
    }

    default <E extends Enum<E>> Optional<E> enumValue(String key, Class<E> enumType) {
        return value(key).map(value -> Enum.valueOf(enumType, value));
    }

    default Optional<Integer> integerValue(String key) {
        return value(key, Integer::valueOf);
    }

    default Optional<Long> longValue(String key) {
        return value(key, Long::valueOf);
    }

    Optional<String> value(String key);

    default <T> Optional<T> value(String key, Function<String, T> converter) {
        return value(key).map(converter);
    }
}
