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

package org.microbule.osgi.config;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import org.microbule.config.api.Config;
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class OsgiConfigProvider implements ConfigProvider, ManagedService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<Config> configRef = new AtomicReference<>(EmptyConfig.INSTANCE);

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        return Stream.of(path).reduce(configRef.get(), Config::filtered, (left, right) -> right);
    }

    @Override
    public String name() {
        return "osgi";
    }

//----------------------------------------------------------------------------------------------------------------------
// ManagedService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties != null) {
            Map<String, String> values = Maps.toMap(Collections.list(properties.keys()), key -> Optional.ofNullable(properties.get(key)).map(String::valueOf).orElse(null));
            configRef.set(new MapConfig(values));
        }
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }
}
