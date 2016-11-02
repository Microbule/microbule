package org.microbule.errormap.impl;

import javax.ws.rs.core.Response;

import com.savoirtech.eos.pattern.whiteboard.KeyedWhiteboard;
import com.savoirtech.eos.pattern.whiteboard.SingleWhiteboard;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.errormap.spi.ErrorMapper;
import org.microbule.errormap.spi.ErrorResponseStrategy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.microbule.errormap.impl.PlainTextErrorResponseStrategy.INSTANCE;

public class ErrorMapperServiceImpl extends KeyedWhiteboard<String, ErrorMapper> implements ErrorMapperService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapperServiceImpl.class);

    private final SingleWhiteboard<ErrorResponseStrategy> responseStrategy;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ErrorMapperServiceImpl(BundleContext bundleContext) {
        super(bundleContext, ErrorMapper.class, (svc, props) -> nameOf(svc.getExceptionType()));
        this.responseStrategy = new SingleWhiteboard<>(bundleContext, ErrorResponseStrategy.class);
    }

    private static String nameOf(Class<?> type) {
        return type.getCanonicalName();
    }

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapperService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Exception createException(Response response) {
        return getErrorResponseStrategy().createException(response);
    }

    @Override
    public Response createResponse(Exception e) {
        ErrorMapper handler = getExceptionHandler(e);
        return getErrorResponseStrategy().createResponse(handler.getStatus(e), handler.getErrorMessages(e));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private ErrorResponseStrategy getErrorResponseStrategy() {
        return responseStrategy.getService(INSTANCE);
    }

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
