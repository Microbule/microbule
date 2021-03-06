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

package org.microbule.util.jaxrs;

import java.util.Optional;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class WebTargetUtilsTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testExtend() {
        final WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/foo");
        final WebTarget extended = WebTargetUtils.extend(target, "bar", "baz");
        assertTrue(extended.getUri().toString().endsWith("/bar/baz"));
    }

    @Test
    public void testIsUtilClass() throws Exception {
        assertIsUtilsClass(WebTargetUtils.class);
    }

    @Test
    public void testParseSuccessfulResponse() {
        Gson gson = new Gson();

        final Response response = Response.ok(gson.toJson(new Person("Slappy", "White"))).type(MediaType.APPLICATION_JSON_TYPE).build();
        final Optional<Person> personOptional = WebTargetUtils.parseJsonResponse(response, Person.class);
        assertTrue(personOptional.isPresent());
        Person person = personOptional.get();
        assertEquals("Slappy", person.getFirstName());
        assertEquals("White", person.getLastName());
    }

    @Test
    public void testParseUnsuccessfulResponse() {
        final Response response = Response.serverError().build();
        final Optional<Person> personOptional = WebTargetUtils.parseJsonResponse(response, Person.class);
        assertFalse(personOptional.isPresent());
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