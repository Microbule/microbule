package org.microbule.container.api;

import org.microbule.config.api.Config;

public interface ServerDefinition {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    String id();

    Config customConfig();

    Object serviceImplementation();

    Class<?> serviceInterface();
}
