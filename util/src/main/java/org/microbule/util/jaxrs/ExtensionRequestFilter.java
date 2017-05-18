package org.microbule.util.jaxrs;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public abstract class ExtensionRequestFilter<T> implements ContainerRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract T createResponse(ContainerRequestContext requestContext);

    protected abstract String markerParam();

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if ("GET".equals(requestContext.getMethod()) && requestContext.getUriInfo().getQueryParameters().containsKey(markerParam())) {
            requestContext.abortWith(
                    Response.ok(createResponse(requestContext))
                            .type(MediaType.APPLICATION_JSON_TYPE)
                            .build());
        }
    }
}
