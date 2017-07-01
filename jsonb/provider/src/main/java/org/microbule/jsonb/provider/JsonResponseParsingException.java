package org.microbule.jsonb.provider;

import javax.json.bind.JsonbException;

import org.microbule.util.exception.MicrobuleException;

public class JsonResponseParsingException extends MicrobuleException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JsonResponseParsingException(JsonbException e) {
        super(e, e.getMessage());
    }
}
