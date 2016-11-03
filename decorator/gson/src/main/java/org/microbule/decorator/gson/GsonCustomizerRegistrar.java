package org.microbule.decorator.gson;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.osgi.framework.BundleContext;

public class GsonCustomizerRegistrar extends AbstractWhiteboard<GsonCustomizer, GsonCustomizer> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonFactory gsonFactory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonCustomizerRegistrar(BundleContext bundleContext, GsonFactory gsonFactory) {
        super(bundleContext, GsonCustomizer.class);
        this.gsonFactory = gsonFactory;
        start();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected GsonCustomizer addService(GsonCustomizer customizer, ServiceProperties serviceProperties) {
        gsonFactory.addCustomizer(customizer);
        return customizer;
    }

    @Override
    protected void removeService(GsonCustomizer customizer, GsonCustomizer customizer2) {
        gsonFactory.removeCustomizer(customizer);
    }
}
