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

import javax.json.bind.Jsonb;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.common.reflect.TypeToken;


public final class WebTargetUtils {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static WebTarget extend(WebTarget baseTarget, String... paths) {
        WebTarget answer = baseTarget;
        for (String path : paths) {
            answer = answer.path(path);
        }
        return answer;
    }

    public static <T> Optional<T> parseResponse(Response response, Jsonb jsonb, TypeToken<T> typeToken) {
        if (Response.Status.OK.getStatusCode() == response.getStatus()) {
            return Optional.of(jsonb.fromJson(response.readEntity(String.class), typeToken.getType()));
        }
        return Optional.empty();
    }

    public static <T> Optional<T> parseResponse(Response response, Jsonb jsonb, Class<T> type) {
        return parseResponse(response, jsonb, TypeToken.of(type));
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private WebTargetUtils() {

    }
}
