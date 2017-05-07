package org.microbule.api;

import org.microbule.config.api.Config;

public interface JaxrsConfigService {
    <T> Config createServerConfig(Class<T> serviceInterface, Config custom);

    <T> Config createServerConfig(Class<T> serviceInterface);

    <T> Config createProxyConfig(Class<T> serviceInterface, Config custom);

    <T> Config createProxyConfig(Class<T> serviceInterface);
}
