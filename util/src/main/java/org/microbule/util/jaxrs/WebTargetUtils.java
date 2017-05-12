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

package org.microbule.util.jaxrs;

import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public final class WebTargetUtils {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Gson GSON = new Gson();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static WebTarget extend(WebTarget baseTarget, String... paths) {
        return Stream.of(paths).reduce(baseTarget, WebTarget::path, (left, right) -> right);
    }

    public static <T> Optional<T> parseJsonResponse(Response response, Class<T> type) {
        return parseJsonResponse(response, TypeToken.get(type));
    }

    public static <T> Optional<T> parseJsonResponse(Response response, TypeToken<T> token) {
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return Optional.of(GSON.fromJson(response.readEntity(String.class), token.getType()));
        }
        return Optional.empty();
    }

    private WebTargetUtils() {

    }
}
