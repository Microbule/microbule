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

package org.microbule.requestlog.decorator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.hello.HelloTestCase;

public class RequestLogDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new RequestLogDecorator());
    }

    @Test
    public void testSayHelloWithoutLogging() {
        Logger.getLogger(RequestLogFilter.class).setLevel(Level.ERROR);
        assertEquals("Hello, Microbule!", createProxy().sayHello("Microbule"));
        Logger.getLogger(RequestLogFilter.class).setLevel(Level.INFO);
    }
}