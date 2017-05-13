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

package org.microbule.config.consul;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Charsets;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.microbule.config.api.Config;
import org.microbule.config.core.ConfigUtils;
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.util.jaxrs.WebTargetUtils;

import static org.microbule.util.jaxrs.WebTargetUtils.extend;

@Singleton
@Named("consulConfigProvider")
public class ConsulConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String RECURSE_PARAM = "recurse";
    private static final String SEPARATOR = "/";
    private static final String DEFAULT_BASE_ADDRESS = "http://localhost:8500/v1/kv/microbule";
    private static final String BASE_ADDRESS_PROP = "baseAddress";
    private static final String NAME = "consul";
    private static final TypeToken<List<ConsulNode>> TYPE_TOKEN = new TypeToken<List<ConsulNode>>() {
    };

    private final WebTarget baseTarget;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConsulConfigProvider() {
        this(ConfigUtils.bootstrapConfig(NAME).value(BASE_ADDRESS_PROP).orElse(DEFAULT_BASE_ADDRESS));
    }

    public ConsulConfigProvider(String baseAddress) {
        baseTarget = ClientBuilder.newClient().target(baseAddress);
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        final Response response = extend(baseTarget, path).queryParam(RECURSE_PARAM, Boolean.TRUE).request(MediaType.APPLICATION_JSON_TYPE).get();
        final String keyPath = Stream.of(path).collect(Collectors.joining(SEPARATOR)) + SEPARATOR;
        return WebTargetUtils.parseJsonResponse(response, TYPE_TOKEN)
                .map(nodes -> nodes.stream().collect(Collectors.toMap(node -> StringUtils.substringAfter(node.getKey(), keyPath), node -> base64Decode(node.getValue()))))
                .map(map -> (Config)new MapConfig(map, SEPARATOR))
                .orElse(EmptyConfig.INSTANCE);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return ConfigUtils.PRIORITY_EXTERNAL;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private String base64Decode(String value) {
        return new String(Base64.getDecoder().decode(value), Charsets.UTF_8);
    }
}
