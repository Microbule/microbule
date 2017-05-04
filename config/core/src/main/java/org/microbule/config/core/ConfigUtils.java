package org.microbule.config.core;

import org.microbule.config.api.Config;

public class ConfigUtils {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static Config bootstrapConfig(String providerName) {
        return new CompositeConfig(new PropertiesConfig(System.getProperties()), new MapConfig(System.getenv()))
                .group("microbule")
                .group(providerName);
    }
}
