package org.microbule.metrics.api;

import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;

public interface MetricsService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    Timer createTimer(String name, String timingStrategyName, Config config);
}
