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
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.spi.GsonBuilderCustomizer;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DefaultGsonServiceTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private GsonBuilderCustomizer customizer;
    private DefaultGsonService service;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initGsonService() {
        final SimpleContainer container = new SimpleContainer();
        service = new DefaultGsonService(container);
        container.initialize();
    }

    @Test
    public void testDoWithGson() {
        service.doWithGson(gson -> {
            assertTrue(gson.htmlSafe());
        });
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

    @Test
    public void testAppending() {
        final Person person = new Person("Hello", "Microbule");
        JsonObject json = service.toJson(person);

        assertEquals(json.toString(), service.append(person, new StringWriter()).toString());
        assertEquals(json.toString(), service.append(person, Person.class.getGenericSuperclass(), new StringWriter()).toString());
        assertEquals(json.toString(), service.append(json, new StringWriter()).toString());
    }

    @Test
    public void testCreateWithGson() {
        final Person person = new Person("Hello", "Microbule");
        final String json = service.createWithGson(gson -> gson.toJson(person));
        assertEquals(json, service.toJsonString(person));
    }

    @Test
    public void testParsing() {
        final Person person = new Person("Hello", "Microbule");
        JsonObject json = service.toJson(person);

        assertJsonEquals(person, service.parse(new StringReader(json.toString()), Person.class));
        assertJsonEquals(person, service.parse(new StringReader(json.toString()), Person.class.getGenericSuperclass()));

        assertJsonEquals(person, service.parse(json.toString(), Person.class));
        assertJsonEquals(person, service.parse(json.toString(), Person.class.getGenericSuperclass()));

        assertJsonEquals(person, service.parse(json, Person.class));
        assertJsonEquals(person, service.parse(json, Person.class.getGenericSuperclass()));
    }

    private <T> void assertJsonEquals(T expected, T actual) {
        assertEquals(service.toJsonString(expected), service.toJsonString(actual));
    }

    @Test
    public void testToJson() {
        final Person person = new Person("Hello", "Microbule");
        JsonObject json = new JsonObject();
        json.addProperty("firstName", "Hello");
        json.addProperty("lastName", "Microbule");

        assertEquals(json, service.toJson(person));
        assertEquals(json, service.toJson(person, Person.class.getGenericSuperclass()));
    }

    @Test
    public void testToJsonString() {
        final Person person = new Person("Hello", "Microbule");
        JsonObject json = new JsonObject();
        json.addProperty("firstName", "Hello");
        json.addProperty("lastName", "Microbule");

        assertEquals(json.toString(), service.toJsonString(person));
        assertEquals(json.toString(), service.toJsonString(person, Person.class.getGenericSuperclass()));
        assertEquals(json.toString(), service.toJsonString(json));
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