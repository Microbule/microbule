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
