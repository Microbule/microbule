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

    Config group(String keyPrefix);

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
