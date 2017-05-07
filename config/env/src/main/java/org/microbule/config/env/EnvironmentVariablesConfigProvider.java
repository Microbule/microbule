package org.microbule.config.env;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.config.spi.ConfigProvider;

@Singleton
@Named("envConfigProvider")
public class EnvironmentVariablesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String SEPARATOR = "_";
    private final Supplier<Map<String,String>> envSupplier;

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
    public Config getConfig(String... path) {
        final Config base = new MapConfig(envSupplier.get(), SEPARATOR);
        return Stream.of(path).reduce(base, Config::group, (left, right) -> right);
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
