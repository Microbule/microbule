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

package org.microbule.core;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.collect.MapMaker;
import org.microbule.annotation.Startup;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;

@Named("jaxrsServerBootstrap")
@Singleton
@Startup
public class JaxrsServerBootstrap implements ServerListener {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final JaxrsServerFactory factory;
    private final Map<String, JaxrsServer> servers = new MapMaker().makeMap();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public JaxrsServerBootstrap(MicrobuleContainer container, JaxrsServerFactory factory) {
        this.factory = factory;
        container.addServerListener(this);
    }

//----------------------------------------------------------------------------------------------------------------------
// ServerListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void registerServer(ServerDefinition serverDefinition) {
        final JaxrsServer server = factory.createJaxrsServer(serverDefinition.serviceInterface(), serverDefinition.serviceImplementation());
        servers.put(serverDefinition.id(), server);
    }

    @Override
    public void unregisterServer(String id) {
        JaxrsServer server = servers.remove(id);
        if (server != null) {
            server.shutdown();
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Canonical Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "JAX-RS Server Bootstrap";
    }
}
