package org.microbule.metrics.core.strategy;

import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;
import org.microbule.metrics.spi.TimingStrategy;

@Named("exponentiallyDecayingTimingStrategy")
@Singleton
public class ExponentiallyDecayingTimingStrategy implements TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String SIZE_PROP = "size";
    private static final String ALPHA_PROP = "alpha";
    private static final int DEFAULT_SIZE = 1028;
    private static final double DEFAULT_ALPHA = 0.015;

//----------------------------------------------------------------------------------------------------------------------
// TimingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(Config config) {
        return new Timer(new ExponentiallyDecayingReservoir(
                config.integerValue(SIZE_PROP).orElse(DEFAULT_SIZE),
                config.doubleValue(ALPHA_PROP).orElse(DEFAULT_ALPHA)));
    }

    @Override
    public String name() {
        return "decay";
    }
}
