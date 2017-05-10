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

package org.microbule.config.etcd;

import java.util.List;

public class EtcdNode {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final String key;
    private final boolean dir;
    private final List<EtcdNode> nodes;
    private final String value;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public EtcdNode(String key, List<EtcdNode> nodes) {
        this(key, true, nodes, null);
    }

    public EtcdNode(String key, String value) {
        this(key, false, null, value);
    }

    public EtcdNode(String key, boolean dir, List<EtcdNode> nodes, String value) {
        this.key = key;
        this.dir = dir;
        this.nodes = nodes;
        this.value = value;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public String getKey() {
        return key;
    }

    public List<EtcdNode> getNodes() {
        return nodes;
    }

    public String getValue() {
        return value;
    }

    public boolean isDir() {
        return dir;
    }
}
