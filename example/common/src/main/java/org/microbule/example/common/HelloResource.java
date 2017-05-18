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

package org.microbule.example.common;

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.microbule.annotation.JaxrsService;
import org.microbule.cache.annotation.Cacheable;
import org.microbule.metrics.annotation.Timed;

@Path("/")
@Api(value = "/", produces = "application/json")
@JaxrsService(name = "hello")
public interface HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Timed(strategy = "timeWindow")
    @Cacheable
    @ApiOperation(value = "Say Hello", notes = "Returns a greeting", response = HelloResponse.class)
    HelloResponse sayHello(@ApiParam(value = "name", required = true) @PathParam("name") @Size(min = 5, message = "Name must be at least 5 characters long.") String name);
}
