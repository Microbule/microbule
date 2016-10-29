package org.microbule.decorator.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

import org.microbule.spi.error.TypedErrorMapper;

public class ConstraintViolationExceptionMapper extends TypedErrorMapper<ConstraintViolationException> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConstraintViolationExceptionMapper() {
        super(ConstraintViolationException.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected List<String> doGetErrorMessages(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
    }

    @Override
    protected Response.Status doGetStatus(ConstraintViolationException exception) {
        return Response.Status.BAD_REQUEST;
    }
}
