package org.microbule.cache.decorator.state;

import java.util.Date;

import javax.ws.rs.core.EntityTag;

public interface ResourceState {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    String ENTITY_TAG_KEY = "ResourceState.entityTag";
    String LAST_MODIFIED_KEY = "ResourceState.lastModified";

    void setLastModified(Date lastUpdated);

    void setEntityTag(EntityTag eTag);

    EntityTag getEntityTag();

    Date getLastModified();
}
