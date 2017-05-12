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

import java.lang.annotation.ElementType;
import java.util.Optional;

import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class ConfigTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testBooleanValue() {
        assertEquals(Boolean.TRUE, new ConstantConfig("true").booleanValue("whatever").get());
    }

    @Test
    public void testDoubleValue() {
        assertEquals(new Double(123.456), new ConstantConfig("123.456").doubleValue("whatever").get());
    }

    @Test
    public void testEnumValue() {
        assertEquals(ElementType.METHOD, new ConstantConfig("METHOD").enumValue("whatever", ElementType.class).get());
    }

    @Test
    public void testIntegerValue() {
        assertEquals(new Integer(123), new ConstantConfig("123").integerValue("whatever").get());
    }

    @Test
    public void testLongValue() {
        assertEquals(new Long(123), new ConstantConfig("123").longValue("whatever").get());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class ConstantConfig implements Config {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String value;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public ConstantConfig(String value) {
            this.value = value;
        }

//----------------------------------------------------------------------------------------------------------------------
// Config Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Config filtered(String... paths) {
            return this;
        }

        @Override
        public Optional<String> value(String key) {
            return Optional.ofNullable(value);
        }
    }
}