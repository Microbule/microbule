package org.microbule.jsonb.spi;

import javax.json.bind.JsonbConfig;

public interface JsonbConfigCustomizer {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void customize(JsonbConfig config);
}
