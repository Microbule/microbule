package org.microbule.config.spi;

import org.microbule.config.api.Config;

public interface ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    int PRIORITY_SYSPROP = -2000;

    int PRIORITY_ENV = -1000;

    int DEFAULT_PRIORITY = 0;

    int EXTERNAL_PRIORITY = 1000;

    Config getConfig(String... path);

    int priority();

    String name();
}
