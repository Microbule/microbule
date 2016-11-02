package org.microbule.errormap.spi;

import java.util.List;

import javax.ws.rs.core.Response;

public interface ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Returns a list of error messages for this exception.
     *
     * @param exception the exception
     * @return a list of error messages
     */
    List<String> getErrorMessages(Exception exception);

    /**
     * Returns the exception type
     *
     * @return the exception type
     */
    Class<? extends Exception> getExceptionType();

    /**
     * Returns the status code.
     *
     * @param exception the exception
     * @return the status
     */
    Response.StatusType getStatus(Exception exception);
}
