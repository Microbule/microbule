package org.microbule.errormap.spi;

import java.util.List;

import javax.ws.rs.core.Response;

public abstract class TypedErrorMapper<E extends Exception> implements ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<E> exceptionType;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected TypedErrorMapper(Class<E> exceptionType) {
        this.exceptionType = exceptionType;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract List<String> doGetErrorMessages(E exception);

    protected abstract Response.StatusType doGetStatus(E exception);

//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getErrorMessages(Exception exception) {
        return doGetErrorMessages((E) exception);
    }

    @Override
    public Class<? extends Exception> getExceptionType() {
        return exceptionType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Response.StatusType getStatus(Exception exception) {
        return doGetStatus((E) exception);
    }
}
