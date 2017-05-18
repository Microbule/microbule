package org.microbule.metrics.core.strategy;

import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.config.core.MapConfig;

public class SlidingTimeWindowTimingStrategyTest extends AbstractTimingStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateTimer() throws ReflectiveOperationException {
        final SlidingTimeWindowTimingStrategy strategy = new SlidingTimeWindowTimingStrategy();
        final Timer timer = strategy.createTimer(new MapConfig());
        SlidingTimeWindowReservoir reservoir = reservoir(timer);
        assertNotNull(reservoir);
        assertEquals("timeWindow", strategy.name());
    }
}