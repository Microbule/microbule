package org.microbule.decorator.gson;

import com.google.gson.GsonBuilder;

public interface GsonCustomizer {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void customize(GsonBuilder builder);
}
