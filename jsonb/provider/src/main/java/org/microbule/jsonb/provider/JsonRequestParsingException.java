package org.microbule.jsonb.provider;

import javax.json.bind.JsonbException;

import org.microbule.util.exception.MicrobuleException;

public class JsonRequestParsingException extends MicrobuleException {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JsonRequestParsingException(JsonbException e) {
        super(e, e.getMessage());
    }
}
