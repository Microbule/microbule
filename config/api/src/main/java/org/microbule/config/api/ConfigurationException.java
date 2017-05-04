package org.microbule.config.api;

public class ConfigurationException extends RuntimeException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConfigurationException(String message, Object... params) {
        super(String.format(message, params));
    }

    public ConfigurationException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }
}
