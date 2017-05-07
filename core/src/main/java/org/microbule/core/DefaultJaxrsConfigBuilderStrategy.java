package org.microbule.core;

import org.microbule.config.api.ConfigBuilder;
import org.microbule.spi.JaxrsConfigBuilderStrategy;

public class DefaultJaxrsConfigBuilderStrategy implements JaxrsConfigBuilderStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULTS_PATH_SEGMENT = "default";
    private static final String SERVER_PATH_SEGMENT = "server";
    private static final String PROXY_PATH_SEGMENT = "proxy";

//----------------------------------------------------------------------------------------------------------------------
// JaxrsConfigBuilderStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public <T> ConfigBuilder buildServerConfig(Class<T> serviceInterface, ConfigBuilder builder) {
        return builder
                .withPath(serviceInterface.getSimpleName(), SERVER_PATH_SEGMENT)
                .withPath(DEFAULTS_PATH_SEGMENT, SERVER_PATH_SEGMENT);
    }

    @Override
    public <T> ConfigBuilder buildProxyConfig(Class<T> serviceInterface, ConfigBuilder builder) {
        return builder
                .withPath(serviceInterface.getSimpleName(), PROXY_PATH_SEGMENT)
                .withPath(DEFAULTS_PATH_SEGMENT, PROXY_PATH_SEGMENT);
    }
}
