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

import java.util.Optional;

import org.microbule.config.api.Config;

public abstract class AbstractConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    protected static final String DEFAULT_SEPARATOR = ".";

    private final String groupPrefix;
    private final String separator;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AbstractConfig(String groupPrefix, String separator) {
        this.groupPrefix = groupPrefix;
        this.separator = separator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Config qualifiedConfig(String qualifiedGroupPrefix, String separator);

    protected abstract Optional<String> qualifiedValue(String qualifiedKey);

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config group(String keyPrefix) {
        return qualifiedConfig(qualify(keyPrefix), separator);
    }

    @Override
    public Optional<String> value(String key) {
        "foo".hashCode();
        return qualifiedValue(qualify(key));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected String qualify(String key) {
        return Optional.ofNullable(groupPrefix).map(prefix -> prefix + separator + key).orElse(key);
    }
}
