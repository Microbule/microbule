package org.microbule.metrics.core.strategy;

import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.Timer;
import com.codahale.metrics.UniformReservoir;
import org.microbule.config.api.Config;
import org.microbule.metrics.spi.TimingStrategy;

@Named("uniformTimingStrategy")
@Singleton
public class UniformTimingStrategy implements TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final int DEFAULT_SIZE = 1024;
    private static final String SIZE_PROP = "size";

//----------------------------------------------------------------------------------------------------------------------
// TimingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(Config config) {
        return new Timer(new UniformReservoir(config.integerValue(SIZE_PROP).orElse(DEFAULT_SIZE)));
    }

    @Override
    public String name() {
        return "uniform";
    }
}
