package org.microbule.jsonb.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.api.PluginListener;
import org.microbule.jsonb.api.JsonbFactory;
import org.microbule.jsonb.spi.JsonbConfigCustomizer;

@Singleton
@Named("jsonbFactory")
public class DefaultJsonbFactory implements JsonbFactory {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<JsonbConfigCustomizer> customizers = new CopyOnWriteArrayList<>();
    private final AtomicReference<Jsonb> jsonbRef = new AtomicReference<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultJsonbFactory(MicrobuleContainer container) {
        container.addPluginListener(JsonbConfigCustomizer.class, new PluginListener<JsonbConfigCustomizer>() {
            @Override
            public boolean registerPlugin(JsonbConfigCustomizer plugin) {
                customizers.add(plugin);
                jsonbRef.set(null);
                return true;
            }

            @Override
            public void unregisterPlugin(JsonbConfigCustomizer plugin) {
                customizers.remove(plugin);
                jsonbRef.set(null);
            }
        });
    }

//----------------------------------------------------------------------------------------------------------------------
// JsonbFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    public Jsonb createJsonb() {
        return jsonbRef.updateAndGet(prev -> {
            if (prev == null) {
                final JsonbConfig config = new JsonbConfig().withFormatting(true).withStrictIJSON(true);
                customizers.forEach(customizer -> customizer.customize(config));
                return JsonbBuilder.create(config);
            }
            return prev;
        });
    }
}
