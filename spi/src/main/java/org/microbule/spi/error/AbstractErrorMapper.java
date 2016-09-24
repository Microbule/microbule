package org.microbule.spi.error;

import java.util.Collections;
import java.util.List;

public abstract class AbstractErrorMapper implements ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// ErrorMapper Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public List<String> getErrorMessages(Exception exception) {
        return Collections.singletonList(exception.getMessage());
    }
}
