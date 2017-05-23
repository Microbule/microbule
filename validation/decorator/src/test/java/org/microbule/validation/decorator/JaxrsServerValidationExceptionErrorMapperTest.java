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

package org.microbule.validation.decorator;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.validation.annotation.payload.StatusProvider;

public class JaxrsServerValidationExceptionErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private JaxrsServerValidationExceptionErrorMapper mapper;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void setUp() {
        mapper = new JaxrsServerValidationExceptionErrorMapper();
    }

    @Test
    public void testErrorCode() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(new ValidatedBean());
        JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);
        int code = mapper.getStatus(exception).getStatusCode();
        assertEquals(400, code);
    }

    @Test
    public void testErrorMessage() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(new ValidatedBean());
        JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);
        final List<String> errorMessages = mapper.getErrorMessages(exception);
        assertEquals(Lists.newArrayList("A cannot be null.", "B cannot be null."), errorMessages);
    }

    @Test
    public void testMapping() {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<Object>> violations = validator.validate(new MyBean());
        final JaxrsServerValidationExceptionErrorMapper mapper = new JaxrsServerValidationExceptionErrorMapper();
        final JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);
        final List<String> messages = mapper.getErrorMessages(exception);
        assertEquals(Lists.newArrayList("Field \"bar\" cannot be empty!", "Field \"foo\" cannot be empty!"), messages);
        assertEquals(Response.Status.BAD_REQUEST, mapper.getStatus(exception));
    }

    @Test
    public void testWithCustomStatusProviderWithMultiplePayloads() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(new CustomErrorCodeBean());
        JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);

        assertEquals(Lists.newArrayList("A cannot be null.", "B cannot be null."), mapper.getErrorMessages(exception));
        assertEquals(401, mapper.getStatus(exception).getStatusCode());
    }

    @Test
    public void testWithCustomStatusProviderWithSinglePayload() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final CustomErrorCodeBean bean = new CustomErrorCodeBean();
        bean.a = "Hello, World!";
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);

        assertEquals(Lists.newArrayList("B cannot be null."), mapper.getErrorMessages(exception));
        assertEquals(403, mapper.getStatus(exception).getStatusCode());
    }

    @Test
    public void testWithInvalidStatusProviderPayload() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final InvalidErrorCodeBean bean = new InvalidErrorCodeBean();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        JaxrsServerValidationException exception = new JaxrsServerValidationException(violations);
        assertEquals(Lists.newArrayList("A cannot be null."), mapper.getErrorMessages(exception));
        assertEquals(400, mapper.getStatus(exception).getStatusCode());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public interface AnotherPayload extends Payload {
    }

    public static class CustomErrorCodeBean {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        @NotNull(message = "A cannot be null.", payload = {StatusProvider.NotAuthorized.class, AnotherPayload.class})
        private String a;

        @NotNull(message = "B cannot be null.", payload = StatusProvider.Forbidden.class)
        private String b;
    }

    public static class InvalidErrorCodeBean {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        @NotNull(message = "A cannot be null.", payload = {InvalidStatusProvider.class})
        private String a;
    }

    public static class InvalidStatusProvider implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        private InvalidStatusProvider() {

        }

//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return null;
        }
    }

    private static class MyBean {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        @NotNull(message = "Field \"foo\" cannot be empty!")
        private String foo;

        @NotNull(message = "Field \"bar\" cannot be empty!")
        private String bar;
    }

    public static class ValidatedBean {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        @NotNull(message = "A cannot be null.")
        private String a;

        @NotNull(message = "B cannot be null.")
        private String b;

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}