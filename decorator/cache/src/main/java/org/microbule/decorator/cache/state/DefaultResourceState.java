package org.microbule.decorator.cache.state;

import java.util.Date;

import javax.ws.rs.core.EntityTag;

import org.apache.cxf.message.Message;

public class DefaultResourceState implements ResourceState {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Message message;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultResourceState(Message message) {
        this.message = message;
    }

//----------------------------------------------------------------------------------------------------------------------
// ResourceState Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public EntityTag getEntityTag() {
        return (EntityTag) message.get(ENTITY_TAG_KEY);
    }

    @Override
    public Date getLastModified() {
        return (Date) message.get(LAST_MODIFIED_KEY);
    }

    @Override
    public void setEntityTag(EntityTag entityTag) {
        message.put(ENTITY_TAG_KEY, entityTag);
    }

    @Override
    public void setLastModified(Date lastModified) {
        message.put(LAST_MODIFIED_KEY, lastModified);
    }
}
