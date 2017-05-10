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

package org.microbule.osgi.container;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.microbule.container.api.PluginListener;
import org.osgi.framework.BundleContext;

public class PluginListenerWhiteboard<P> extends AbstractWhiteboard<P, P> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final PluginListener<P> pluginListener;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public PluginListenerWhiteboard(BundleContext bundleContext, Class<P> pluginType, PluginListener<P> pluginListener) {
        super(bundleContext, pluginType);
        this.pluginListener = pluginListener;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected P addService(P service, ServiceProperties props) {
        if (pluginListener.registerPlugin(service)) {
            return service;
        }
        return null;
    }

    @Override
    protected void removeService(P service, P tracked) {
        pluginListener.unregisterPlugin(service);
    }
}
