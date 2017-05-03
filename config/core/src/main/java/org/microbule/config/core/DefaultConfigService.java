package org.microbule.config.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.spi.ConfigProvider;

public class DefaultConfigService extends AbstractConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<String, ConfigProvider> providers;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultConfigService(BeanFinder beanFinder, String overrideProviderNamesCsv, String providerNamesCsv, long waitDuration, TimeUnit waitUnit) {
        super(overrideProviderNamesCsv, providerNamesCsv, waitDuration, waitUnit);
        this.providers = beanFinder.beanMap(ConfigProvider.class, ConfigProvider::name);
    }

    public DefaultConfigService(BeanFinder beanFinder, List<String> overrideProviderNames, List<String> providerNames, long waitDuration, TimeUnit waitUnit) {
        super(overrideProviderNames, providerNames, waitDuration, waitUnit);
        this.providers = beanFinder.beanMap(ConfigProvider.class, ConfigProvider::name);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected ConfigProvider lookupProvider(String name) {
        return providers.get(name);
    }
}
