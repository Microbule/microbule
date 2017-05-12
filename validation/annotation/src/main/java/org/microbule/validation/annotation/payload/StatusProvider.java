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

package org.microbule.validation.annotation.payload;

import javax.validation.Payload;
import javax.ws.rs.core.Response;

@FunctionalInterface
public interface StatusProvider extends Payload {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Response.Status status();

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    class BadRequest implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return Response.Status.BAD_REQUEST;
        }
    }

    class Conflict implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return Response.Status.CONFLICT;
        }
    }

    class Forbidden implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return Response.Status.FORBIDDEN;
        }
    }

    class NotAuthorized implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return Response.Status.UNAUTHORIZED;
        }
    }

    class NotFound implements StatusProvider {
//----------------------------------------------------------------------------------------------------------------------
// StatusProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public Response.Status status() {
            return Response.Status.NOT_FOUND;
        }
    }
}
