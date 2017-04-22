package org.microbule.gson.core;

import org.microbule.gson.spi.GsonCustomizer;

public interface GsonCustomizerRegistry {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void addCustomizer(GsonCustomizer customizer);

    void removeCustomizer(GsonCustomizer customizer);
}
