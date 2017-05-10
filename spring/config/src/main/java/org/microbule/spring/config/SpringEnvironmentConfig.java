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

package org.microbule.spring.config;

import java.util.Optional;

import org.microbule.config.api.Config;
import org.microbule.config.core.AbstractConfig;
import org.springframework.core.env.Environment;

public class SpringEnvironmentConfig extends AbstractConfig {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Environment environment;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public SpringEnvironmentConfig(Environment environment) {
        this(environment, null, DEFAULT_SEPARATOR);
    }

    private SpringEnvironmentConfig(Environment environment, String groupPrefix, String separator) {
        super(groupPrefix, separator);
        this.environment = environment;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Config qualifiedConfig(String qualifiedGroupPrefix, String separator) {
        return new SpringEnvironmentConfig(environment, qualifiedGroupPrefix, separator);
    }

    @Override
    protected Optional<String> qualifiedValue(String qualifiedKey) {
        return Optional.ofNullable(environment.getProperty(qualifiedKey));
    }
}
