package org.microbule.decorator.validation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.logging.NoOpFaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.hibernate.validator.HibernateValidator;
import org.microbule.spi.JaxrsServerConfig;
import org.microbule.spi.JaxrsServerDecorator;

@Provider
public class ValidationProvider extends AbstractPhaseInterceptor<Message> implements ContainerRequestFilter, JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String METHOD_KEY = "org.apache.cxf.resource.method";
    private final Validator validator = Validation.byDefaultProvider().providerResolver(() -> Collections.singletonList(new HibernateValidator())).configure().buildValidatorFactory().getValidator();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ValidationProvider() {
        super(Phase.PRE_INVOKE);
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        InterceptorChain chain = PhaseInterceptorChain.getCurrentMessage().getInterceptorChain();
        chain.add(this);
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
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServerConfig server) {
        server.addProvider(this);
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
