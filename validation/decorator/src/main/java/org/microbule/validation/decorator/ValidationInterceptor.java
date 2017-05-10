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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.logging.FaultListener;
import org.apache.cxf.logging.NoOpFaultListener;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.microbule.validation.annotation.Groups;

public class ValidationInterceptor extends AbstractPhaseInterceptor<Message> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Class<?>[] DEFAULT_GROUPS = {Default.class};
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
        final Class<?>[] groups = Optional.ofNullable(method.getAnnotation(Groups.class)).map(Groups::value).orElse(DEFAULT_GROUPS);
        final Set<ConstraintViolation<T>> violations = methodValidator.validateParameters(instance, method, arguments, groups);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
