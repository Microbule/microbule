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

package org.microbule.errormap.impl.text;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.errormap.impl.AbstractErrorResponseStrategyTest;
import org.microbule.errormap.spi.ErrorResponseStrategy;

public class PlainTextErrorResponseStrategyTest extends AbstractErrorResponseStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected RuntimeException createException(Response.Status status, String message) {
        return PlainTextErrorResponseStrategy.INSTANCE.createException(Response.status(status).entity(message).build());
    }

    @Test
    public void testCreateResponse() {
        final ErrorResponseStrategy strategy = PlainTextErrorResponseStrategy.INSTANCE;
        final Response response = strategy.createResponse(Response.Status.FORBIDDEN, Lists.newArrayList("You are forbidden!", "Not gonna happen!"));
        assertEquals("You are forbidden!\nNot gonna happen!", response.readEntity(String.class));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}