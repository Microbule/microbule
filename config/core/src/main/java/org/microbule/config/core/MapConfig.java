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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.microbule.config.api.Config;

import static java.util.Optional.ofNullable;

public class MapConfig extends AbstractConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<String, String> values;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MapConfig() {
        this(new HashMap<>(), null, DEFAULT_SEPARATOR);
    }

    public MapConfig(Map<String, String> values) {
        this(new HashMap<>(values), null, DEFAULT_SEPARATOR);
    }

    public MapConfig(Map<String, String> values, String separator) {
        this(new HashMap<>(values), null, separator);
    }

    private MapConfig(Map<String, String> values, String groupPrefix, String separator) {
        super(groupPrefix, separator);
        this.values = values;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public MapConfig addValue(String key, String value) {
        values.put(qualify(key), value);
        return this;
    }

    @Override
    protected Config qualifiedConfig(String qualifiedGroupPrefix, String separator) {
        return new MapConfig(values, qualifiedGroupPrefix, separator);
    }

    @Override
    protected Optional<String> qualifiedValue(String qualifiedKey) {
        return ofNullable(values.get(qualifiedKey));
    }
}
