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

package org.microbule.gson.core;

import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.spi.GsonCustomizer;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DefaultGsonServiceTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private GsonCustomizer customizer;

    @Test
    public void testToJson() {
        final SimpleContainer container = new SimpleContainer();
        final DefaultGsonService service = new DefaultGsonService(container);
        container.initialize();

        final StringWriter sw = new StringWriter();
        service.toJson(new Person("Hello", "Microbule"), Person.class, sw);

        final String json = sw.toString();

        final Person clone = service.fromJson(new StringReader(json), Person.class);

        assertEquals("Hello", clone.getFirstName());
        assertEquals("Microbule", clone.getLastName());
    }

    @Test
    public void testAddCustomizer() {
        doAnswer(invocation -> {
            GsonBuilder builder = invocation.getArgument(0);
            builder.disableHtmlEscaping();
            return null;
        }).when(customizer).customize(any(GsonBuilder.class));
        final SimpleContainer container = new SimpleContainer();
        final DefaultGsonService service = new DefaultGsonService(container);
        container.addBean(customizer);
        container.initialize();
        verify(customizer).customize(any(GsonBuilder.class));
        assertFalse(service.getGson().htmlSafe());
        container.shutdown();
        verifyNoMoreInteractions(customizer);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class Person {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String firstName;
        private final String lastName;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}