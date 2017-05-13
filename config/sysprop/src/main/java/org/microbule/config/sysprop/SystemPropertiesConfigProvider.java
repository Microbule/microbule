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

package org.microbule.config.sysprop;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.core.ConfigUtils;
import org.microbule.config.spi.ConfigProvider;

@Singleton
@Named("systemPropertiesConfigProvider")
public class SystemPropertiesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String NAME = "sysprop";

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        return ConfigUtils.fromProperties(System.getProperties()).filtered(path);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return ConfigUtils.PRIORITY_SYSPROP;
    }
}
