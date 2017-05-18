package org.microbule.metrics.core.strategy;

import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;
import org.microbule.metrics.spi.TimingStrategy;

public class DefaultTimingStrategy implements TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final TimingStrategy INSTANCE = new DefaultTimingStrategy();

//----------------------------------------------------------------------------------------------------------------------
// Named Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String name() {
        return "default";
    }

//----------------------------------------------------------------------------------------------------------------------
// TimingStrategy Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(Config config) {
        return new Timer();
    }
}
