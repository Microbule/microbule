package org.microbule.spi;

import org.microbule.config.api.ConfigBuilder;

public interface JaxrsConfigBuilderStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    <T> ConfigBuilder buildProxyConfig(Class<T> serviceInterface, ConfigBuilder builder);
    <T> ConfigBuilder buildServerConfig(Class<T> serviceInterface, ConfigBuilder builder);
}
