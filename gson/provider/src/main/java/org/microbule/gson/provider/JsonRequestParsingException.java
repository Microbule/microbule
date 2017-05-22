package org.microbule.gson.provider;

import com.google.gson.JsonSyntaxException;
import org.microbule.util.exception.MicrobuleException;

public class JsonRequestParsingException extends MicrobuleException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JsonRequestParsingException(JsonSyntaxException e) {
        super(e, e.getMessage());
    }
}
