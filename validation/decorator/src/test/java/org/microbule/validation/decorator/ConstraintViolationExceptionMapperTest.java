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
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class ConstraintViolationExceptionMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testMapping() {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        final Set<ConstraintViolation<MyBean>> violations = validator.validate(new MyBean());
        final ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
        final ConstraintViolationException exception = new ConstraintViolationException(violations);
        final List<String> messages = mapper.getErrorMessages(exception);
        Assert.assertEquals(Lists.newArrayList("Field \"bar\" cannot be empty!", "Field \"foo\" cannot be empty!"), messages);
        assertEquals(Response.Status.BAD_REQUEST, mapper.getStatus(exception));
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class MyBean {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        @NotNull(message = "Field \"foo\" cannot be empty!")
        private String foo;

        @NotNull(message = "Field \"bar\" cannot be empty!")
        private String bar;
    }
}