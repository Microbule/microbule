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

package org.microbule.scheduler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.microbule.scheduler.api.RefreshableReference;
import org.microbule.scheduler.api.Refresher;
import org.microbule.scheduler.core.DefaultSchedulerService;
import org.microbule.test.core.MockObjectTestCase;

import static org.awaitility.Awaitility.await;

public class DefaultSchedulerServiceTest extends MockObjectTestCase {

    @Test
    public void testInitialValue() {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);

        final RefreshableReference<Long> reference = service.createRefreshableReference(currentValue -> currentValue == null ? 0 : currentValue + 1, 1, TimeUnit.MINUTES);
        reference.cancel();
        assertEquals(Long.valueOf(0), reference.get());

    }

    @Test
    public void testRefreshedValue() {
        final DefaultSchedulerService service = new DefaultSchedulerService();

        final RefreshableReference<Long> reference = service.createRefreshableReference(currentValue -> currentValue == null ? 0 : currentValue + 1, 75, TimeUnit.MILLISECONDS);
        final long expiration = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(100);
        await().until(() -> System.nanoTime() >= expiration);
        reference.cancel();
        assertEquals(Long.valueOf(1), reference.get());
    }

    @Test
    public void testCancelRefresh() {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);

        final RefreshableReference<Long> reference = service.createRefreshableReference(currentValue -> currentValue == null ? 0 : currentValue + 1, 75, TimeUnit.MILLISECONDS);
        final long expiration = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(100);
        reference.cancel();
        await().until(() -> System.nanoTime() >= expiration);
        assertEquals(Long.valueOf(0), reference.get());
    }

    @Test
    public void testRefreshException() {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);

        final RefreshableReference<Long> reference = service.createRefreshableReference(new Refresher<Long>() {
            private final AtomicBoolean errored = new AtomicBoolean(false);
            @Override
            public Long refresh(Long currentValue) {
                if(currentValue != null && currentValue == 1 && !errored.get()) {
                    errored.set(true);
                    throw new RuntimeException("Not going to update!");
                }
                return currentValue == null ? 0 : currentValue + 1;
            }
        }, 20, TimeUnit.MILLISECONDS);
        final long expiration = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(100);
        await().until(() -> System.nanoTime() >= expiration);
        reference.cancel();
        assertTrue(reference.get() > 1);
    }


}