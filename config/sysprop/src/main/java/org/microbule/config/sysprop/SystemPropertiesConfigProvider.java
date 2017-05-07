package org.microbule.config.sysprop;

import java.util.stream.Stream;

import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.config.core.PropertiesConfig;
import org.microbule.config.spi.ConfigProvider;

@Singleton
@Named("systemPropertiesConfigProvider")
public class SystemPropertiesConfigProvider implements ConfigProvider {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String NAME = "sysprop";

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Config getConfig(String... path) {
        Config base = new PropertiesConfig(System.getProperties());
        return Stream.of(path).reduce(base, Config::group, (left, right) -> right);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int priority() {
        return PRIORITY_SYSPROP;
    }
}
