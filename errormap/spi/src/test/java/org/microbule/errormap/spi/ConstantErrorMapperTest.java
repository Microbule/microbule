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

package org.microbule.errormap.spi;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class ConstantErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testMapping() {
        final ConstantErrorMapper mapper = new ConstantErrorMapper(SQLException.class, Response.Status.INTERNAL_SERVER_ERROR);
        assertEquals(SQLException.class, mapper.getExceptionType());
        final SQLException exception = new SQLException("Oops!");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, mapper.getStatus());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, mapper.getStatus(exception));
        assertEquals(Lists.newArrayList("Oops!"), mapper.getErrorMessages(exception));
    }
}