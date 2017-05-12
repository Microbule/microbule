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

package org.microbule.spring.container;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;

@Named("collector")
@Singleton
public class Collector {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<MyPlugin> plugins;
    private final List<ServerDefinition> serverDefinitions = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public Collector(MicrobuleContainer container) {
        this.plugins = container.pluginList(MyPlugin.class);
        container.addServerListener(new ServerListener() {
            @Override
            public void registerServer(ServerDefinition serverDefinition) {
                serverDefinitions.add(serverDefinition);
            }

            @Override
            public void unregisterServer(String id) {
                serverDefinitions.removeIf(def -> id.equals(def.id()));
            }
        });
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public List<MyPlugin> getPlugins() {
        return plugins;
    }

    public List<ServerDefinition> getServerDefinitions() {
        return serverDefinitions;
    }
}
