package org.microbule.validation.decorator;

import java.util.Collections;

import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

public class ValidationDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ValidationFeature feature;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ValidationDecorator() {
        this.feature = new ValidationFeature(newValidator());
    }

    private static Validator newValidator() {
        return Validation.byDefaultProvider().providerResolver(() -> Collections.singletonList(new HibernateValidator())).configure().buildValidatorFactory().getValidator();
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addFeature(feature);
    }

    @Override
    public String name() {
        return "validation";
    }
}
