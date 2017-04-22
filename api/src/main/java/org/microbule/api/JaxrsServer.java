package org.microbule.api;

@FunctionalInterface
public interface JaxrsServer {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Shuts down the JAX-RS server.
     */
    void shutdown();
}
