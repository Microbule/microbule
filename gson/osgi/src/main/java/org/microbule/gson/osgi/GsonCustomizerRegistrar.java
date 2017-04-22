package org.microbule.gson.osgi;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.microbule.gson.core.GsonCustomizerRegistry;
import org.microbule.gson.spi.GsonCustomizer;
import org.osgi.framework.BundleContext;

public class GsonCustomizerRegistrar extends AbstractWhiteboard<GsonCustomizer, GsonCustomizer> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonCustomizerRegistry registry;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonCustomizerRegistrar(BundleContext bundleContext, GsonCustomizerRegistry registry) {
        super(bundleContext, GsonCustomizer.class);
        this.registry = registry;
        start();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected GsonCustomizer addService(GsonCustomizer customizer, ServiceProperties serviceProperties) {
        registry.addCustomizer(customizer);
        return customizer;
    }

    @Override
    protected void removeService(GsonCustomizer customizer, GsonCustomizer customizer2) {
        registry.removeCustomizer(customizer);
    }
}
