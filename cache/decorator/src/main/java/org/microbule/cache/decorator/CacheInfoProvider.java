package org.microbule.cache.decorator;

import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.ext.ContextProvider;
import org.apache.cxf.message.Message;
import org.microbule.cache.decorator.state.DefaultResourceState;
import org.microbule.cache.decorator.state.ResourceState;

@Provider
public class CacheInfoProvider implements ContextProvider<ResourceState> {
//----------------------------------------------------------------------------------------------------------------------
// ContextProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ResourceState createContext(Message message) {
        return new DefaultResourceState(message);
    }
}
