package org.microbule.container.api;

public interface ServerListener {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void registerServer(ServerDefinition serverDefinition);

    void unregisterServer(String id);
}
