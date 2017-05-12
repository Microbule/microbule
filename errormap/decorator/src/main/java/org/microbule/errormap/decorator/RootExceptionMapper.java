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

package org.microbule.errormap.decorator;

import javax.ws.rs.ext.Provider;

import org.microbule.errormap.api.ErrorMapperService;

@Provider
public class RootExceptionMapper extends ErrorMapperExceptionMapper<Exception> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public RootExceptionMapper(ErrorMapperService errorMapperService, String strategy) {
        super(errorMapperService, strategy);
    }
}
