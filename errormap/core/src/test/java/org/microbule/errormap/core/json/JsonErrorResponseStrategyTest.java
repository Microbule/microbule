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

package org.microbule.errormap.core.json;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.errormap.core.AbstractErrorResponseStrategyTest;

public class JsonErrorResponseStrategyTest extends AbstractErrorResponseStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected RuntimeException createException(Response.Status status, String message) {
        final JsonErrorResponse response = new JsonErrorResponse(status.getStatusCode(), Lists.newArrayList(message));
        return new JsonErrorResponseStrategy().createException(Response.status(status).entity(response).build());
    }

    @Test
    public void testCreateResponse() {
        final JsonErrorResponseStrategy strategy = new JsonErrorResponseStrategy();
        final Response response = strategy.createResponse(Response.Status.FORBIDDEN, Lists.newArrayList("You are forbidden!", "Not gonna happen!"));
        final JsonErrorResponse errorResponse = response.readEntity(JsonErrorResponse.class);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), errorResponse.getStatus());
        assertEquals(Lists.newArrayList("You are forbidden!", "Not gonna happen!"), errorResponse.getMessages());
    }

    @Test
    public void testName() {
        final JsonErrorResponseStrategy strategy = new JsonErrorResponseStrategy();
        assertEquals("json", strategy.name());
    }
}