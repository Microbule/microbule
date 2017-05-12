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

package org.microbule.config.etcd;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.microbule.config.api.Config;
import org.microbule.config.core.ConfigUtils;
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;
import org.microbule.util.jaxrs.WebTargetUtils;

import static org.microbule.util.jaxrs.WebTargetUtils.extend;

@Singleton
@Named("etcdConfigProvider")
public class EtcdConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String SEPARATOR = "/";
    private static final String NAME = "etcd";
    private static final String BASE_ADDRESS_PROP = "baseAddress";
    private static final String DEFAULT_BASE_ADDRESS = "http://localhost:2379/v2/keys/microbule";

    private final WebTarget baseTarget;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public EtcdConfigProvider() {
        this(ConfigUtils.bootstrapConfig(NAME).value(BASE_ADDRESS_PROP).orElse(DEFAULT_BASE_ADDRESS));
    }

    public EtcdConfigProvider(String baseAddress) {
        baseTarget = ClientBuilder.newClient().target(baseAddress);
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String name() {
        return NAME;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public Config getConfig(String... path) {
        final Response response = extend(baseTarget, path).queryParam("recursive", "true").request(MediaType.APPLICATION_JSON_TYPE).get();
        final String keyPath = Stream.of(path).collect(Collectors.joining(SEPARATOR)) + SEPARATOR;
        final Optional<EtcdResponse> etcdResponse = WebTargetUtils.parseJsonResponse(response, EtcdResponse.class);
        return etcdResponse.map(r -> {
            Map<String, String> values = new HashMap<>();
            fillMap(values, r.getNode(), keyPath);
            return (Config)new MapConfig(values, "/");
        }).orElse(EmptyConfig.INSTANCE);
    }

    @Override
    public int priority() {
        return PRIORITY_EXTERNAL;
    }

    private void fillMap(Map<String, String> values, EtcdNode node, String keyPath) {
        if (node != null) {
            if (node.isDir()) {
                node.getNodes().forEach(child -> fillMap(values, child, keyPath));
            } else {
                values.put(StringUtils.substringAfter(node.getKey(), keyPath), node.getValue());
            }
        }
    }
}
