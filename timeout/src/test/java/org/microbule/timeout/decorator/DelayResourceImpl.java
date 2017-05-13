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

package org.microbule.timeout.decorator;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.awaitility.Awaitility.await;

public class DelayResourceImpl implements DelayResource {
//----------------------------------------------------------------------------------------------------------------------
// DelayResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DelayResourceImpl.class);

    @Override
    public String delay(long value) {
        LOGGER.info("Delaying {} ms...", value);
        final long expiration = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(value);
        await().until(() -> System.nanoTime() >= expiration);
        return String.valueOf(value);
    }
}
