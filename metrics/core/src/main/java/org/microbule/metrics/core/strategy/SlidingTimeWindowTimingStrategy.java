package org.microbule.metrics.core.strategy;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;
import org.microbule.metrics.spi.TimingStrategy;

@Named("slidingTimeWindowTimingStrategy")
@Singleton
public class SlidingTimeWindowTimingStrategy implements TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final TimeUnit DEFAULT_WINDOW_UNIT = TimeUnit.MINUTES;
    private static final long DEFAULT_WINDOW = 15;
    private static final String WINDOW_PROP = "window";
    private static final String WINDOW_UNIT_PROP = "windowUnit";

//----------------------------------------------------------------------------------------------------------------------
// TimingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(Config config) {
        return new Timer(new SlidingTimeWindowReservoir(
                config.longValue(WINDOW_PROP).orElse(DEFAULT_WINDOW),
                config.enumValue(WINDOW_UNIT_PROP, TimeUnit.class).orElse(DEFAULT_WINDOW_UNIT)));
    }

    @Override
    public String name() {
        return "timeWindow";
    }
}
