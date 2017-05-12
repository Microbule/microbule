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

package org.microbule.config.http;

import java.util.stream.Stream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.microbule.config.api.Config;
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.spi.ConfigProvider;
import org.slf4j.Logger;

public abstract class HttpConfigProvider<R> implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Gson GSON = new GsonBuilder().create();


    private final TypeToken<R> responseType;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected HttpConfigProvider(Class<R> responseType) {
        this.responseType = TypeToken.get(responseType);
    }

    protected HttpConfigProvider(TypeToken<R> responseType) {
        this.responseType = responseType;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract WebTarget createTarget(String... paths);

    protected abstract Logger getLogger();

    protected abstract Config toConfig(R response, String... paths);

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... paths) {
        final WebTarget target = createTarget(paths);
        getLogger().info("Loading configuration from {}.", target.getUri());
        final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            final R webResponse = GSON.fromJson(response.readEntity(String.class), responseType.getType());
            return toConfig(webResponse, paths);
        }
        return EmptyConfig.INSTANCE;
    }

    @Override
    public int priority() {
        return EXTERNAL_PRIORITY;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected Config dereferenceGroups(Config base, String... groups) {
        return Stream.of(groups).reduce(base, Config::filtered, (c1, c2) -> c2);
    }

    protected WebTarget extend(WebTarget baseTarget, String... paths) {
        return Stream.of(paths).reduce(baseTarget, WebTarget::path, (left, right) -> right);
    }
}
