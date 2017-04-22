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

    public DefaultConfigService(String overrideProviderNamesCsv, String providerNamesCsv, long waitDuration, TimeUnit waitUnit) {
        super(overrideProviderNamesCsv, providerNamesCsv, waitDuration, waitUnit);
    }

    public DefaultConfigService(List<String> overrideProviderNames, List<String> providerNames, long waitDuration, TimeUnit waitUnit) {
        super(overrideProviderNames, providerNames, waitDuration, waitUnit);
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
