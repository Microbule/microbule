package org.microbule.metrics.decorator;

import java.io.StringReader;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.core.DefaultGsonService;
import org.microbule.gson.decorator.GsonProxyDecorator;
import org.microbule.gson.decorator.GsonServerDecorator;
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
    private DefaultGsonService gsonService;

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
        gsonService = new DefaultGsonService(container);
        container.addBean(gsonService);
        container.addBean(new MetricsGsonCustomizer());
        container.addBean(new GsonServerDecorator(gsonService));
        container.addBean(new GsonProxyDecorator(gsonService));
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

    @Test
    public void testMetricsFilter() {
        Response response = createWebTarget().queryParam("_metrics", "").request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        MetricsResponse metricsResponse = gsonService.fromJson(new StringReader(response.readEntity(String.class)), MetricsResponse.class);

        metricsResponse.getTimers().forEach((k,v) -> {
            assertTrue(k.startsWith("TimedResource"));
        });
    }

    @Test
    public void testNoMetricsWhenErrorOccurs() {
        try {
            createProxy().errorTimer();
            fail("Should encounter an error!");
        }
        catch(InternalServerErrorException e) {
            // Do nothing!
        }

        final Timer timer = metricsService.getRegistry().timer("TimedResource.errorTimer");
        assertEquals(0, timer.getCount());
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

        @Override
        public String errorTimer() {
            throw new InternalServerErrorException("Oops!");
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        private String now() {
            return String.valueOf(System.currentTimeMillis());
        }
    }
}