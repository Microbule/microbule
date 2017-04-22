package org.microbule.validation.decorator;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.logging.NoOpFaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * Created by jcarman on 4/19/17.
 */
public class ValidationInterceptor extends AbstractPhaseInterceptor<Message> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String METHOD_KEY = "org.apache.cxf.resource.method";
    private final Validator validator;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    ValidationInterceptor(Validator validator) {
        super(Phase.PRE_INVOKE);
        this.validator = validator;
    }

//----------------------------------------------------------------------------------------------------------------------
// Interceptor Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void handleMessage(Message message) throws Fault {
        try {
            final List<Object> arguments = MessageContentsList.getContentsList(message);
            if (arguments.size() > 0) {
                final Object serviceObject = message.getExchange().get(Message.SERVICE_OBJECT);
                final Method method = (Method) message.get(METHOD_KEY);
                validateParameters(serviceObject, method, arguments.toArray());
            }
        } catch (ValidationException e) {
            message.put(FaultListener.class.getName(), new NoOpFaultListener());
            throw e;
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private <T> void validateParameters(final T instance, final Method method, final Object[] arguments) {
        final ExecutableValidator methodValidator = validator.forExecutables();
        final Set<ConstraintViolation<T>> violations = methodValidator.validateParameters(instance, method, arguments);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
