package org.microbule.osgi.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuietTimeLatch {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(QuietTimeLatch.class);
    private final AtomicLong lastUpdated;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final ScheduledExecutorService executorService;
    private final long quietTime;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public QuietTimeLatch(final long quietTimeInMs) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.quietTime = TimeUnit.MILLISECONDS.toNanos(quietTimeInMs);
        this.lastUpdated = new AtomicLong(System.nanoTime());
        checkLastUpdate();
    }

    private void checkLastUpdate() {
        final long elapsed = System.nanoTime() - lastUpdated.get();
        if (elapsed >= quietTime) {
            LOGGER.info("Quiet time ({} ms) expired, releasing latch...", TimeUnit.NANOSECONDS.toMillis(quietTime));
            latch.countDown();
            executorService.shutdownNow();
        } else {
            executorService.schedule(this::checkLastUpdate, quietTime - elapsed, TimeUnit.NANOSECONDS);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void await() {
        Uninterruptibles.awaitUninterruptibly(latch);
    }

    public void updated() {
        lastUpdated.set(System.nanoTime());
    }
}
