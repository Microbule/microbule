package org.microbule.config.osgi;

import java.util.concurrent.TimeUnit;

import com.savoirtech.eos.pattern.whiteboard.KeyedWhiteboard;
import org.microbule.config.core.AbstractConfigService;
import org.microbule.config.spi.ConfigProvider;
import org.osgi.framework.BundleContext;

public class OsgiConfigService extends AbstractConfigService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String NAME_PROP = "name";
    private final KeyedWhiteboard<String, ConfigProvider> whiteboard;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OsgiConfigService(BundleContext bundleContext, String overrideProviderNamesCsv, String providerNamesCsv, long waitDuration, TimeUnit waitUnit) {
        super(overrideProviderNamesCsv, providerNamesCsv, waitDuration, waitUnit);
        whiteboard = new KeyedWhiteboard<>(bundleContext, ConfigProvider.class, (svc, props) -> props.getProperty(NAME_PROP));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected ConfigProvider lookupProvider(String name) {
        return whiteboard.getService(name);
    }
}
