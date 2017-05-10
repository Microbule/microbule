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

package org.microbule.config.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.microbule.config.api.Config;

public class RecordedConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Config config;
    private final Map<QualifiedKey, Optional<String>> recordedValues;
    private final List<String> paths;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RecordedConfig(Config config) {
        this(config, new HashMap<>(), Collections.emptyList());
    }

    private RecordedConfig(Config config, Map<QualifiedKey, Optional<String>> recordedValues, List<String> paths) {
        this.config = config;
        this.recordedValues = recordedValues;
        this.paths = paths;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        final List<String> childPaths = new LinkedList<>(paths);
        childPaths.add(keyPrefix);
        return new RecordedConfig(config.group(keyPrefix), recordedValues, childPaths);
    }

    @Override
    public Optional<String> value(String key) {
        final Optional<String> value = config.value(key);
        recordedValues.put(new QualifiedKey(paths, key), value);
        return value;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    boolean matches(Config other) {
        return recordedValues.entrySet().stream().allMatch(entry -> entry.getKey().get(other).equals(entry.getValue()));
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class QualifiedKey {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final List<String> paths;
        private final String key;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public QualifiedKey(List<String> paths, String key) {
            this.paths = paths;
            this.key = key;
        }

//----------------------------------------------------------------------------------------------------------------------
// Canonical Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            QualifiedKey that = (QualifiedKey) o;

            return new EqualsBuilder()
                    .append(paths, that.paths)
                    .append(key, that.key)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(paths)
                    .append(key)
                    .toHashCode();
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        public Optional<String> get(Config config) {
            return paths.stream().reduce(config, Config::group, (left, right) -> right).value(key);
        }
    }
}
