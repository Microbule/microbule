package org.microbule.decorator.cache.resource;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.microbule.decorator.cache.state.ResourceState;


public class CacheResourceImpl implements CacheResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Context
    private ResourceState resourceState;

//----------------------------------------------------------------------------------------------------------------------
// CacheResource Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public String getValueWithEtag() {
        resourceState.setEntityTag(ENTITY_TAG);
        return "ValueWithEtag";
    }

    @Override
    public String getValueWithLastModified() {
        resourceState.setLastModified(LAST_MODIFIED);
        return "ValueWithLastModified";
    }

    @Override
    public String getValueWithNoState() {
        return "ValueWithNoState";
    }

    @Override
    public String getValueWithLastModifiedAndEtag() {
        resourceState.setEntityTag(ENTITY_TAG);
        resourceState.setLastModified(LAST_MODIFIED);
        return "ValueWithLastModifiedAndEtag";
    }

    @Override
    public Response createResponse() {
        return Response.ok(System.currentTimeMillis()).lastModified(LAST_MODIFIED).tag(ENTITY_TAG).build();
    }
}
