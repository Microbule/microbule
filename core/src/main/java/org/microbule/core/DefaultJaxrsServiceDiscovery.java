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

import org.microbule.api.JaxrsConfigService;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigurationException;
import org.microbule.spi.JaxrsAddressChooser;
import org.microbule.spi.JaxrsServiceDiscovery;

public class DefaultJaxrsServiceDiscovery implements JaxrsServiceDiscovery {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String PROXY_ADDRESS_PROP = "proxyAddress";

    private final JaxrsConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultJaxrsServiceDiscovery(JaxrsConfigService configService) {
        this.configService = configService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDiscovery Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsAddressChooser createEndpointChooser(Class<?> serviceInterface, String serviceName) {
        final Config config = configService.createProxyConfig(serviceInterface, serviceName);
        final String baseAddress = config.value(PROXY_ADDRESS_PROP).orElseThrow(() -> new ConfigurationException("Missing '%s' property.", PROXY_ADDRESS_PROP));
        return () -> baseAddress;
    }
}
