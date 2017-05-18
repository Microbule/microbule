package org.microbule.metrics.core;

import com.codahale.metrics.Timer;
import org.junit.Before;
import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.container.core.SimpleContainer;
import org.microbule.metrics.spi.TimingStrategy;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultMetricsServiceTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private TimingStrategy strategy;

    @Mock
    private Config config;

    @Mock
    private Config strategyConfig;

    private DefaultMetricsService service;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initContainer() {
        when(strategy.name()).thenReturn("mockStrategy");
        when(strategy.createTimer(any(Config.class))).thenReturn(new Timer());

        SimpleContainer container = new SimpleContainer();

        container.addBean(strategy);

        service = new DefaultMetricsService(container);

        container.initialize();
    }

    @Test
    public void testCreateTimerWithUnknown() {
        final Timer timer = service.createTimer("foo", "bogus", new MapConfig());
        assertNotNull(timer);

        assertNotNull(service.getRegistry());
        verify(strategy).name();
        Mockito.verifyNoMoreInteractions(strategy);
    }

    @Test
    public void testCreateTimer() {
        when(config.filtered("mockStrategy")).thenReturn(strategyConfig);

        final Timer timer = service.createTimer("foo", "mockStrategy", config);
        assertNotNull(timer);

        assertNotNull(service.getRegistry());
        verify(strategy, times(2)).name();
        verify(strategy).createTimer(strategyConfig);
        Mockito.verifyNoMoreInteractions(strategy);
    }
}