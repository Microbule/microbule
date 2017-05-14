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

/**
 * A <code>Config</code> provides access to configuration values.  They values are arranged in a hierarchy
 * organized by "paths".
 */
public interface Config {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Get a Boolean value
     *
     * @param key the value key
     * @return the optional Boolean value
     */
    default Optional<Boolean> booleanValue(String key) {
        return value(key, Boolean::valueOf);
    }

    /**
     * Get a Double value
     *
     * @param key the value key
     * @return the optional Double value
     */
    default Optional<Double> doubleValue(String key) {
        return value(key, Double::valueOf);
    }

    /**
     * Get an enum value
     *
     * @param key      the value key
     * @param enumType the enum type
     * @param <E>      the enum type
     * @return the optional enum value
     */
    default <E extends Enum<E>> Optional<E> enumValue(String key, Class<E> enumType) {
        return value(key).map(value -> Enum.valueOf(enumType, value));
    }

    /**
     * Filter this {@link Config} object using the specified path
     *
     * @param paths the path
     * @return a new {@link Config} object containing only values at the specifed path or below
     */
    Config filtered(String... paths);

    /**
     * Get an Integer value
     *
     * @param key the value key
     * @return the optional Integer value
     */
    default Optional<Integer> integerValue(String key) {
        return value(key, Integer::valueOf);
    }

    /**
     * Get a Long value
     *
     * @param key the value key
     * @return the optional Long value
     */
    default Optional<Long> longValue(String key) {
        return value(key, Long::valueOf);
    }

    /**
     * Get a String value
     *
     * @param key the value key
     * @return the optional String value
     */
    Optional<String> value(String key);

    /**
     * Get a value by converting it to the target type using the specified converter
     *
     * @param key       the value key
     * @param converter the converter
     * @param <T>       the target type
     * @return the optional target type value
     */
    default <T> Optional<T> value(String key, Function<String, T> converter) {
        return value(key).map(converter);
    }
}
