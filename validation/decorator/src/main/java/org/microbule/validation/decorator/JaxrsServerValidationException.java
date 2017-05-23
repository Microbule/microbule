package org.microbule.validation.decorator;

import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;

public class JaxrsServerValidationException extends RuntimeException {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Set<ConstraintViolation<Object>> violations;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerValidationException(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public Stream<ConstraintViolation<Object>> violations() {
        return violations.stream();
    }
}
