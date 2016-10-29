package org.microbule.decorator.errormap.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.savoirtech.eos.pattern.whiteboard.KeyedWhiteboard;
import com.savoirtech.eos.pattern.whiteboard.SingleWhiteboard;
import org.apache.commons.lang3.StringUtils;
import org.microbule.decorator.errormap.DefaultErrorMapper;
import org.microbule.decorator.errormap.ErrorMapperService;
import org.microbule.spi.error.ErrorMapper;
import org.microbule.spi.error.ErrorResponseProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorMapperServiceImpl extends KeyedWhiteboard<String, ErrorMapper> implements ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapperServiceImpl.class);

    private static final ErrorResponseProvider DEFAULT_ERROR_RESPONSE_PROVIDER = (status, errorMessages) ->
            Response.status(status)
                    .type(MediaType.TEXT_PLAIN_TYPE)
                    .entity(StringUtils.join(errorMessages, "\n"))
                    .build();

    private final SingleWhiteboard<ErrorResponseProvider> responseFactory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperServiceImpl(BundleContext bundleContext) {
        super(bundleContext, ErrorMapper.class, (svc, props) -> nameOf(svc.getExceptionType()));
        this.responseFactory = new SingleWhiteboard<>(bundleContext, ErrorResponseProvider.class);
    }

    private static String nameOf(Class<?> type) {
        return type.getCanonicalName();
    }

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapperService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Response createResponse(Exception e) {
        ErrorMapper handler = getExceptionHandler(e);
        return responseFactory.getService(DEFAULT_ERROR_RESPONSE_PROVIDER).createResponse(handler.getStatus(e), handler.getErrorMessages(e));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private ErrorMapper getExceptionHandler(Exception exception) {
        final ErrorMapper handler = findExceptionHandler(exception.getClass());
        if (!handler.getExceptionType().isInstance(exception)) {
            final Bundle expectedBundle = FrameworkUtil.getBundle(handler.getExceptionType());
            final Bundle actualBundle = FrameworkUtil.getBundle(exception.getClass());
            LOGGER.warn("Mismatched exception type {} expected bundle {} ({}), actual bundle {} ({}).", nameOf(handler.getExceptionType()), expectedBundle.getSymbolicName(), expectedBundle.getBundleId(), actualBundle.getSymbolicName(), actualBundle.getBundleId());
            return DefaultErrorMapper.INSTANCE;
        }
        return handler;
    }

    private ErrorMapper findExceptionHandler(Class<?> targetType) {
        if (Exception.class.equals(targetType)) {
            return DefaultErrorMapper.INSTANCE;
        }
        ErrorMapper mapper = getService(nameOf(targetType));
        if (mapper == null) {
            return findExceptionHandler(targetType.getSuperclass());
        }
        return mapper;
    }
}
