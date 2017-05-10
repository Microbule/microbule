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

package org.microbule.container.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import org.apache.commons.lang3.ClassUtils;
import org.microbule.container.api.ServerDefinition;

public class SimpleContainer extends StaticContainer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Set<Object> beans = new CopyOnWriteArraySet<>();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void addBean(Object bean) {
        beans.add(bean);
    }

    @Override
    protected <B> Iterable<B> plugins(Class<B> pluginType) {
        return beans.stream()
                .filter(pluginType::isInstance)
                .map(pluginType::cast)
                .collect(Collectors.toList());
    }

    @Override
    protected Iterable<ServerDefinition> servers() {
        return beans.stream()
                .flatMap(bean -> ClassUtils.getAllInterfaces(bean.getClass()).stream()
                        .filter(serviceInterface -> serviceInterface.isAnnotationPresent(Path.class))
                        .map(serviceInterface -> new DefaultServerDefinition(String.valueOf(System.identityHashCode(bean)), serviceInterface, bean)))
                .collect(Collectors.toList());
    }
}
