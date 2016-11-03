package org.microbule.decorator.gson;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.osgi.framework.BundleContext;

public class GsonFactory extends AbstractWhiteboard<GsonCustomizer, GsonCustomizer> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<GsonCustomizer> customizers = new CopyOnWriteArrayList<>();
    private final AtomicReference<Gson> gson = new AtomicReference<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonFactory(BundleContext bundleContext) {
        super(bundleContext, GsonCustomizer.class);
        rebuild();
        start();
    }

    private void rebuild() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        customizers.forEach(customizer -> customizer.customize(builder));
        gson.set(builder.create());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected GsonCustomizer addService(GsonCustomizer service, ServiceProperties properties) {
        customizers.add(service);
        rebuild();
        return service;
    }

    public Gson createGson() {
        return gson.get();
    }

    @Override
    protected void removeService(GsonCustomizer service, GsonCustomizer tracked) {
        customizers.remove(tracked);
        rebuild();
    }
}
