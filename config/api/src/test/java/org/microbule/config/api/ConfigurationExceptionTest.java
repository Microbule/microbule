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

package org.microbule.config.api;

import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class ConfigurationExceptionTest extends MicrobuleTestCase {

    @Test
    public void testConstructor() {
        final ConfigurationException exception = new ConfigurationException("Hello, %s!", "Microbule");
        assertEquals("Hello, Microbule!", exception.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        final IllegalArgumentException cause = new IllegalArgumentException("oops!");
        final ConfigurationException exception = new ConfigurationException(cause, "Hello, %s!", "Microbule");
        assertEquals("Hello, Microbule!", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}