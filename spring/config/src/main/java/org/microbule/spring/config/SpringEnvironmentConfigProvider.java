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

import java.util.stream.Stream;

import org.microbule.config.api.Config;
import org.microbule.config.spi.ConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringEnvironmentConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String NAME = "spring";

    @Autowired
    private Environment environment;

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        Config base = new SpringEnvironmentConfig(environment);
        return Stream.of(path).reduce(base, Config::group, (left, right) -> right);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }
}
