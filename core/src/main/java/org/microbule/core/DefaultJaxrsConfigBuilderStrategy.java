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

package org.microbule.core;

import org.microbule.spi.JaxrsConfigBuilder;
import org.microbule.spi.JaxrsConfigBuilderStrategy;

public class DefaultJaxrsConfigBuilderStrategy implements JaxrsConfigBuilderStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULTS_PATH_SEGMENT = "default";
    private static final String SERVER_PATH_SEGMENT = "server";
    private static final String PROXY_PATH_SEGMENT = "proxy";

//----------------------------------------------------------------------------------------------------------------------
// JaxrsConfigBuilderStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> JaxrsConfigBuilder<T> buildProxyConfig(JaxrsConfigBuilder<T> builder) {
        final String serviceName = builder.serviceName();
        return builder.withPath(serviceName, PROXY_PATH_SEGMENT)
                .withPath(serviceName)
                .withPath(DEFAULTS_PATH_SEGMENT, PROXY_PATH_SEGMENT);
    }

    @Override
    public <T> JaxrsConfigBuilder<T> buildServerConfig(JaxrsConfigBuilder<T> builder) {
        final String serviceName = builder.serviceName();
        return builder.withPath(serviceName, SERVER_PATH_SEGMENT)
                .withPath(serviceName)
                .withPath(DEFAULTS_PATH_SEGMENT, SERVER_PATH_SEGMENT);
    }
}
