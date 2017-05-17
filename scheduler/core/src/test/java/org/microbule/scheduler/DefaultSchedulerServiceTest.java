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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.awaitility.Awaitility;
import org.junit.Test;
import org.microbule.scheduler.api.RefreshableReference;
import org.microbule.scheduler.api.Refresher;
import org.microbule.scheduler.api.Scheduled;
import org.microbule.scheduler.core.DefaultSchedulerService;
import org.microbule.test.core.MockObjectTestCase;

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
        final RefreshableReference<Long> reference = service.createRefreshableReference(currentValue -> currentValue == null ? 0 : currentValue + 1, 100, TimeUnit.MILLISECONDS);
        await(150);
        reference.cancel();
        assertEquals(Long.valueOf(1), reference.get());
    }

    private void await(long duration) {
        await(5, duration);
    }

    private void await(long pollInterval, long duration) {
        final long expiration = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(duration);
        Awaitility.await().pollInterval(pollInterval, TimeUnit.MILLISECONDS).pollDelay(pollInterval, TimeUnit.MILLISECONDS).until(() -> System.nanoTime() >= expiration);
    }

    @Test
    public void testCancelRefresh() {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);

        final RefreshableReference<Long> reference = service.createRefreshableReference(currentValue -> currentValue == null ? 0 : currentValue + 1, 25, TimeUnit.MILLISECONDS);
        reference.cancel();
        await(40);
        assertEquals(Long.valueOf(0), reference.get());
    }

    @Test
    public void testRefreshException() {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);

        final RefreshableReference<Long> reference = service.createRefreshableReference(new Refresher<Long>() {
            private final AtomicBoolean errored = new AtomicBoolean(false);

            @Override
            public Long refresh(Long currentValue) {
                if (currentValue != null && currentValue == 1 && !errored.get()) {
                    errored.set(true);
                    throw new RuntimeException("Not going to update!");
                }
                return currentValue == null ? 0 : currentValue + 1;
            }
        }, 20, TimeUnit.MILLISECONDS);
        await(100);
        reference.cancel();
        assertTrue(reference.get() > 1);
    }

    @Test
    public void testSchedule() throws Exception {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);
        CountDownLatch latch = new CountDownLatch(1);
        final Scheduled schedule = service.schedule(latch::countDown, 25, TimeUnit.MILLISECONDS);
        assertTrue(latch.await(50, TimeUnit.MILLISECONDS));
    }

    @Test(expected = RejectedExecutionException.class)
    public void testShutdown() throws Exception {
        final DefaultSchedulerService service = new DefaultSchedulerService(1);
        service.shutdownScheduler();
        CountDownLatch latch = new CountDownLatch(1);
        service.schedule(latch::countDown, 25, TimeUnit.MILLISECONDS);
    }

}