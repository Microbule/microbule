package org.microbule.metrics.decorator;

import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.metrics.core.DefaultMetricsService;
import org.microbule.metrics.core.strategy.ExponentiallyDecayingTimingStrategy;
import org.microbule.metrics.core.strategy.SlidingTimeWindowTimingStrategy;
import org.microbule.metrics.core.strategy.SlidingWindowTimingStrategy;
import org.microbule.metrics.core.strategy.UniformTimingStrategy;
import org.microbule.test.server.JaxrsServerTestCase;

public class MetricsDecoratorTest extends JaxrsServerTestCase<TimedResource> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private DefaultMetricsService metricsService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        metricsService = new DefaultMetricsService(container);
        container.addBean(metricsService);
        container.addBean(new ExponentiallyDecayingTimingStrategy());
        container.addBean(new SlidingTimeWindowTimingStrategy());
        container.addBean(new SlidingWindowTimingStrategy());
        container.addBean(new UniformTimingStrategy());
        container.addBean(new MetricsDecorator(metricsService));
    }

    @Override
    protected TimedResource createImplementation() {
        return new TimedResourceImpl();
    }

    @Test
    public void testDecayTiming() {
        assertNotNull(createProxy().decayTimer());

        final Timer timer = metricsService.getRegistry().timer("TimedResource.decayTimer");
        assertEquals(1, timer.getCount());
    }

    @Test
    public void testDefaultTiming() {
        assertNotNull(createProxy().defaultTimer());

        final Timer timer = metricsService.getRegistry().timer("TimedResource.defaultTimer");
        assertEquals(1, timer.getCount());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class TimedResourceImpl implements TimedResource {
//----------------------------------------------------------------------------------------------------------------------
// TimedResource Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public String decayTimer() {
            return now();
        }

        @Override
        public String defaultTimer() {
            return now();
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        private String now() {
            return String.valueOf(System.currentTimeMillis());
        }
    }
}