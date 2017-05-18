package org.microbule.metrics.core.strategy;

import com.codahale.metrics.Timer;
import com.codahale.metrics.UniformReservoir;
import org.junit.Test;
import org.microbule.config.core.MapConfig;

public class UniformTimingStrategyTest extends AbstractTimingStrategyTest {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testCreateTimer() throws ReflectiveOperationException {
        final UniformTimingStrategy strategy = new UniformTimingStrategy();
        final Timer timer = strategy.createTimer(new MapConfig());
        UniformReservoir reservoir = reservoir(timer);
        assertNotNull(reservoir);
        assertEquals("uniform", strategy.name());
    }
}