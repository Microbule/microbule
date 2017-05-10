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

package org.microbule.errormap.spi;

import java.util.List;

import javax.ws.rs.core.Response;

public interface ErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Returns a list of error messages for this exception.
     *
     * @param exception the exception
     * @return a list of error messages
     */
    List<String> getErrorMessages(Exception exception);

    /**
     * Returns the exception type
     *
     * @return the exception type
     */
    Class<? extends Exception> getExceptionType();

    /**
     * Returns the status code.
     *
     * @param exception the exception
     * @return the status
     */
    Response.StatusType getStatus(Exception exception);
}
