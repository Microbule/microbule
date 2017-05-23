/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.gson.core;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.api.PluginListener;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.spi.GsonBuilderCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("gsonService")
public class DefaultGsonService implements GsonService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGsonService.class);
    private final List<GsonBuilderCustomizer> customizers = new CopyOnWriteArrayList<>();
    private final AtomicReference<Gson> gson = new AtomicReference<>(new GsonBuilder().create());

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultGsonService(MicrobuleContainer container) {
        container.addPluginListener(GsonBuilderCustomizer.class, new GsonCustomizerListener());
    }

//----------------------------------------------------------------------------------------------------------------------
// GsonService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> T fromJson(Reader json, Type type) {
        return getGson().fromJson(json, type);
    }

    @Override
    public void toJson(Object src, Type type, Appendable writer) {
        getGson().toJson(src, type, writer);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Gson getGson() {
        return gson.get();
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private class GsonCustomizerListener implements PluginListener<GsonBuilderCustomizer> {
//----------------------------------------------------------------------------------------------------------------------
// PluginListener Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public boolean registerPlugin(GsonBuilderCustomizer plugin) {

            customizers.add(plugin);
            LOGGER.debug("Rebuilding GSON instance to include plugin {}.", plugin);
            rebuild();
            return true;
        }

        @Override
        public void unregisterPlugin(GsonBuilderCustomizer plugin) {
            customizers.remove(plugin);
            LOGGER.debug("Rebuilding GSON instance without plugin {}.", plugin);
            rebuild();
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        private void rebuild() {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            customizers.forEach(customizer -> customizer.customize(builder));
            gson.set(builder.create());
        }
    }
}
