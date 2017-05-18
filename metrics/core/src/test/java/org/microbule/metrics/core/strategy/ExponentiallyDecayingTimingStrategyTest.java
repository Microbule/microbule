package org.microbule.metrics.core.strategy;

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.config.core.MapConfig;

public class ExponentiallyDecayingTimingStrategyTest extends AbstractTimingStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateTimer() throws Exception {
        final ExponentiallyDecayingTimingStrategy strategy = new ExponentiallyDecayingTimingStrategy();
        final Timer timer = strategy.createTimer(new MapConfig());

        ExponentiallyDecayingReservoir reservoir = reservoir(timer);
        assertNotNull(reservoir);
        assertEquals("decay", strategy.name());
    }
}