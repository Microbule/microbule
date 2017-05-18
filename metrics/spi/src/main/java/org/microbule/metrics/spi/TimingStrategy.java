package org.microbule.metrics.spi;

import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;

public interface TimingStrategy {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Timer createTimer(Config config);

    String name();
}
