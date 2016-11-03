package org.microbule.decorator.gson;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<GsonCustomizer> customizers = new CopyOnWriteArrayList<>();
    private final AtomicReference<Gson> gson = new AtomicReference<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonFactory() {
        rebuild();
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

    public void addCustomizer(GsonCustomizer customizer) {
        customizers.add(customizer);
        rebuild();
    }

    public Gson createGson() {
        return gson.get();
    }

    public void removeCustomizer(GsonCustomizer customizer) {
        if (customizers.remove(customizer)) {
            rebuild();
        }
    }
}
