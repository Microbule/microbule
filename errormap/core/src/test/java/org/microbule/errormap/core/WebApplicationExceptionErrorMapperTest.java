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

package org.microbule.errormap.core;

import java.util.Collections;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class WebApplicationExceptionErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetErrorMessages() {
        final WebApplicationExceptionErrorMapper errorMapper = new WebApplicationExceptionErrorMapper();
        assertEquals(Collections.singletonList("Not found!"), errorMapper.doGetErrorMessages(new NotFoundException("Not found!")));
        assertEquals(Collections.singletonList("Not Found"), errorMapper.doGetErrorMessages(new NotFoundException("")));
    }

    @Test
    public void testGetStatus() {
        final WebApplicationExceptionErrorMapper errorMapper = new WebApplicationExceptionErrorMapper();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), errorMapper.getStatus(new NotFoundException()).getStatusCode());
    }
}