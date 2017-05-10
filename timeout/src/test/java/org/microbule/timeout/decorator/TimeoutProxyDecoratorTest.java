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

package org.microbule.timeout.decorator;

import javax.ws.rs.ProcessingException;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.JaxrsServerTestCase;

public class TimeoutProxyDecoratorTest extends JaxrsServerTestCase<DelayResource> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new TimeoutProxyDecorator());
    }

    @Override
    protected void configureProxy(MapConfig proxyConfig) {
        proxyConfig.addValue("timeout.connectionTimeout", "100");
        proxyConfig.addValue("timeout.receiveTimeout", "500");
    }

    @Override
    protected DelayResource createImplementation() {
        return new DelayResourceImpl();
    }

    @Test(expected = ProcessingException.class)
    public void testConnectionTimeoutExpired() {
        final DelayResource proxy = createProxy();
        proxy.delay(3000);
    }

    @Test
    public void testConnectionTimeout() {
        final DelayResource proxy = createProxy();
        Assert.assertEquals("100", proxy.delay(100));
    }
}