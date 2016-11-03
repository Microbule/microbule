package org.microbule.core.providers;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class RequestCountClientFilter implements ClientRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final AtomicLong requestCount = new AtomicLong();

//----------------------------------------------------------------------------------------------------------------------
// ClientRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestCount.incrementAndGet();
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public AtomicLong getRequestCount() {
        return requestCount;
    }
}
