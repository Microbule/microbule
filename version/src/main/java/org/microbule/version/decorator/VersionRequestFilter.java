package org.microbule.version.decorator;

import javax.ws.rs.container.ContainerRequestContext;

import org.microbule.util.jaxrs.ExtensionRequestFilter;

public class VersionRequestFilter extends ExtensionRequestFilter<VersionResponse> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final VersionResponse response;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public VersionRequestFilter(VersionResponse response) {
        this.response = response;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected VersionResponse createResponse(ContainerRequestContext requestContext) {
        return response;
    }

    @Override
    protected String markerParam() {
        return "_version";
    }
}
