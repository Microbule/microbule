package org.microbule.osgi.beanfinder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.microbule.beanfinder.api.BeanFinderListener;
import org.microbule.beanfinder.core.AbstractBeanFinder;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsgiBeanFinder extends AbstractBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(OsgiBeanFinder.class);

    private final BundleContext bundleContext;
    private final AtomicLong lastUpdated;
    private final ScheduledExecutorService scheduler;
    private final long quietPeriod;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public OsgiBeanFinder(BundleContext bundleContext, long quietPeriodInMs) {
        this.bundleContext = bundleContext;
        this.quietPeriod = TimeUnit.MILLISECONDS.toNanos(quietPeriodInMs);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.lastUpdated = new AtomicLong(System.nanoTime());
        scheduler.schedule(this::checkLastUpdate, quietPeriodInMs, TimeUnit.NANOSECONDS);
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> void findBeans(Class<B> beanType, BeanFinderListener<B> listener) {
        final BeanFinderListenerWhiteboard<B> whiteboard = new BeanFinderListenerWhiteboard<>(bundleContext, beanType, listener, lastUpdated);
        whiteboard.start();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private void checkLastUpdate() {
        final long elapsed = System.nanoTime() - lastUpdated.get();
        if (elapsed >= quietPeriod) {
            LOGGER.debug("Quiet period ({} ms) expired.", TimeUnit.NANOSECONDS.toMillis(quietPeriod));
            complete();
            scheduler.shutdownNow();
        } else {
            final long newDelay = quietPeriod - elapsed;
            LOGGER.debug("Only {} ms have elapsed since last update time, checking again in {} ms...", TimeUnit.NANOSECONDS.toMillis(quietPeriod), TimeUnit.NANOSECONDS.toMillis(newDelay));
            scheduler.schedule(this::checkLastUpdate, newDelay, TimeUnit.NANOSECONDS);
        }
    }
}
