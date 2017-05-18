package org.microbule.metrics.core.strategy;

import com.codahale.metrics.SlidingWindowReservoir;
import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.config.core.MapConfig;

public class SlidingWindowTimingStrategyTest extends AbstractTimingStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateTimer() throws ReflectiveOperationException {
        final SlidingWindowTimingStrategy strategy = new SlidingWindowTimingStrategy();
        final Timer timer = strategy.createTimer(new MapConfig());
        SlidingWindowReservoir reservoir = reservoir(timer);
        assertNotNull(reservoir);
        assertEquals("window", strategy.name());
    }
}