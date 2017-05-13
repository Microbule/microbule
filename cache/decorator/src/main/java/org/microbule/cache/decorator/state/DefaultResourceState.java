/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.cache.decorator.state;

import java.util.Date;

import javax.ws.rs.core.EntityTag;

import org.apache.cxf.message.Message;

public class DefaultResourceState implements ResourceState {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENTITY_TAG_KEY = "ResourceState.entityTag";
    private static final String LAST_MODIFIED_KEY = "ResourceState.lastModified";
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
