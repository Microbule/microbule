package org.microbule.config.env;

import java.util.Map;
import java.util.function.Supplier;

import javax.inject.Named;

import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;

@Named
public class EnvironmentVariablesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private Supplier<Map<String,String>> envSupplier;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public EnvironmentVariablesConfigProvider() {
        this(System::getenv);
    }

    public EnvironmentVariablesConfigProvider(Supplier<Map<String, String>> envSupplier) {
        this.envSupplier = envSupplier;
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getProxyConfig(Class<?> serviceInterface) {
        return envConfig(serviceInterface, "proxy");
    }

    @Override
    public Config getServerConfig(Class<?> serviceInterface) {
        return envConfig(serviceInterface, "server");
    }

    @Override
    public String name() {
        return "env";
    }

    @Override
    public int priority() {
        return PRIORITY_ENV;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Config envConfig(Class<?> serviceInterface, String subtype) {
        return new MapConfig(envSupplier.get(), "_")
                .group("microbule")
                .group(serviceInterface.getSimpleName())
                .group(subtype);
    }
}
