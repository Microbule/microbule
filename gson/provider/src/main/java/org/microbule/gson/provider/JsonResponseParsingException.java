package org.microbule.gson.provider;

import com.google.gson.JsonSyntaxException;
import org.microbule.util.exception.MicrobuleException;

public class JsonResponseParsingException extends MicrobuleException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JsonResponseParsingException(JsonSyntaxException e) {
        super(e, e.getMessage());
    }
}
