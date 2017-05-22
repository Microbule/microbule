package org.microbule.util.exception;

import static java.lang.String.format;

public class MicrobuleException extends RuntimeException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MicrobuleException(String message, Object... params) {
        super(format(message, params));
    }

    public MicrobuleException(Throwable cause, String message, Object... params) {
        super(format(message, params), cause);
    }
}
