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

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class JsonErrorResponseTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final JsonErrorResponse response = new JsonErrorResponse(404, Lists.newArrayList("one", "two", "three"));
        assertEquals(404, response.getStatus());
        assertEquals(Lists.newArrayList("one", "two", "three"), response.getMessages());
    }
}