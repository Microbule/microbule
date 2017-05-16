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

package org.microbule.scheduler.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.scheduler.api.RefreshableReference;
import org.microbule.scheduler.api.Refresher;
import org.microbule.scheduler.api.Scheduled;
import org.microbule.scheduler.api.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("refreshService")
@Singleton
public class DefaultSchedulerService implements SchedulerService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_SCHEDULER_THREADS = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSchedulerService.class);

    private final ScheduledExecutorService scheduler;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DefaultSchedulerService() {
        this(DEFAULT_SCHEDULER_THREADS);
    }

    public DefaultSchedulerService(int schedulerThreads) {
        final AtomicInteger count = new AtomicInteger();
        scheduler = Executors.newScheduledThreadPool(schedulerThreads, r -> new Thread(r, "Microbule Scheduler Thread " + count.incrementAndGet()));
    }

//----------------------------------------------------------------------------------------------------------------------
// SchedulerService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <T> RefreshableReference<T> createRefreshableReference(Refresher<T> refresher, long delay, TimeUnit unit) {
        final AtomicReference<T> ref = new AtomicReference<>(refresher.refresh(null));
        LOGGER.debug("Scheduling {} every {} {}...", refresher, delay, unit);
        final ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(() -> {
            try {
                ref.set(refresher.refresh(ref.get()));
            } catch (RuntimeException e) {
                LOGGER.error("Error occurred while refreshing using refresher {}.", refresher, e);
            }
        }, delay, delay, unit);
        return new DefaultRefreshableReference<>(ref, future);
    }

    @Override
    public Scheduled schedule(Runnable task, long delay, TimeUnit unit) {
        final ScheduledFuture<?> future = scheduler.scheduleWithFixedDelay(task, delay, delay, unit);
        return () -> future.cancel(true);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdownNow();
    }
}
