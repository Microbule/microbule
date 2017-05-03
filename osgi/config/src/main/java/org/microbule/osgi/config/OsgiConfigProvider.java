package org.microbule.osgi.config;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Maps;
import org.microbule.config.api.Config;
import org.microbule.config.core.EmptyConfig;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class OsgiConfigProvider implements ConfigProvider, ManagedService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicReference<Config> configRef = new AtomicReference<>(EmptyConfig.INSTANCE);

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return configRef.get().group(serviceInterface.getSimpleName()).group("proxy");
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return configRef.get().group(serviceInterface.getSimpleName()).group("server");
    }

    @Override
    public String name() {
        return "osgi";
    }

//----------------------------------------------------------------------------------------------------------------------
// ManagedService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (properties != null) {
            Map<String, String> values = Maps.toMap(Collections.list(properties.keys()), key -> Optional.ofNullable(properties.get(key)).map(String::valueOf).orElse(null));
            configRef.set(new MapConfig(values));
        }
    }
}
