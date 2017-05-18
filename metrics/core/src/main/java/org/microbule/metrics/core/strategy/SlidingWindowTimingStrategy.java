package org.microbule.metrics.core.strategy;

import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.SlidingWindowReservoir;
import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;
import org.microbule.metrics.spi.TimingStrategy;

@Named("slidingWindowTimingStrategy")
@Singleton
public class SlidingWindowTimingStrategy implements TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_SIZE = 1024;
    private static final String SIZE_PROP = "size";

//----------------------------------------------------------------------------------------------------------------------
// Named Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String name() {
        return "window";
    }

//----------------------------------------------------------------------------------------------------------------------
// TimingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(Config config) {
        return new Timer(new SlidingWindowReservoir(config.integerValue(SIZE_PROP).orElse(DEFAULT_SIZE)));
    }
}
