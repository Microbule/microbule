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

package org.microbule.cache.decorator.resource;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.microbule.cache.decorator.state.ResourceState;

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
