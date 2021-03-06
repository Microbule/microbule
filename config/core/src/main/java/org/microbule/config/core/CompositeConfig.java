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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.microbule.config.api.Config;

public class CompositeConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<Config> members;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CompositeConfig(Config... members) {
        this(Arrays.asList(members));
    }

    public CompositeConfig(List<Config> members) {
        this.members = members;
    }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

    public static Config create(List<Config> members) {
        switch (members.size()) {
            case 0:
                return EmptyConfig.INSTANCE;
            case 1:
                return members.get(0);
            default:
                return new CompositeConfig(members);
        }
    }

    @Override
    public Config filtered(String... paths) {
        return new CompositeConfig(members.stream().map(config -> config.filtered(paths)).collect(Collectors.toList()));
    }

    @Override
    public Optional<String> value(String key) {
        return members.stream()
                .map(member -> member.value(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
