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

package org.microbule.tracer.decorator;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.server.hello.HelloTestCase;

public class TracerServerDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new TracerServerDecorator(container));
    }

    @Test
    public void testWithCustomTraceId() {
        final WebTarget target = createWebTarget();
        final Response response = target.path("hello").path("Microbule")
                .request(MediaType.TEXT_PLAIN)
                .header(AbstractTracerDecorator.DEFAULT_TRACE_ID_HEADER, "foobarbaz")
                .get();
        assertEquals(200, response.getStatus());
        Assert.assertEquals("foobarbaz", response.getHeaderString(AbstractTracerDecorator.DEFAULT_TRACE_ID_HEADER));
        Assert.assertNotNull(response.getHeaderString(AbstractTracerDecorator.DEFAULT_REQUEST_ID_HEADER));
    }
}