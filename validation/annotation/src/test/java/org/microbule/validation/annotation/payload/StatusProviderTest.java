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

package org.microbule.validation.annotation.payload;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class StatusProviderTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testBadRequest() {
        assertEquals(Response.Status.BAD_REQUEST, new StatusProvider.BadRequest().status());
    }

    @Test
    public void testConfict() {
        assertEquals(Response.Status.CONFLICT, new StatusProvider.Conflict().status());
    }

    @Test
    public void testForbidden() {
        assertEquals(Response.Status.FORBIDDEN, new StatusProvider.Forbidden().status());
    }

    @Test
    public void testNotFound() {
        assertEquals(Response.Status.NOT_FOUND, new StatusProvider.NotFound().status());
    }

    @Test
    public void testUnauthorized() {
        assertEquals(Response.Status.UNAUTHORIZED, new StatusProvider.NotAuthorized().status());
    }
}