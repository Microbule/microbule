package org.microbule.config.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;
import org.microbule.config.spi.ConfigProvider;

public class DefaultConfigService extends AbstractConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<String, ConfigProvider> providers = new MapMaker().makeMap();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultConfigService(List<String> providerNames, long waitDuration, TimeUnit waitUnit) {
        super(providerNames, waitDuration, waitUnit);
    }

    public DefaultConfigService(String providerNamesCsv, long waitDuration, TimeUnit waitUnit) {
        super(providerNamesCsv, waitDuration, waitUnit);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected ConfigProvider lookupProvider(String name) {
        return providers.get(name);
    }

    public boolean registerConfigProvider(String name, ConfigProvider provider) {
        return providers.putIfAbsent(name, provider) == null;
    }
}
