package org.microbule.decorator.validation;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.MockObjectTestCase;

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
        assertEquals(Lists.newArrayList("Field \"bar\" cannot be empty!", "Field \"foo\" cannot be empty!"), messages);
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