package org.microbule.metrics.core;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.metrics.api.MetricsService;
import org.microbule.metrics.core.strategy.DefaultTimingStrategy;
import org.microbule.metrics.spi.TimingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("metricsService")
@Singleton
public class DefaultMetricsService implements MetricsService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMetricsService.class);
    private final MetricRegistry registry = new MetricRegistry();
    private final Map<String, TimingStrategy> timingStrategies;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public DefaultMetricsService(MicrobuleContainer container) {
        this.timingStrategies = container.pluginMap(TimingStrategy.class, TimingStrategy::name);
    }

//----------------------------------------------------------------------------------------------------------------------
// MetricsService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Timer createTimer(String name, String timingStrategyName, Config config) {
        return registry.timer(name, () -> {
            TimingStrategy strategy = timingStrategies.get(timingStrategyName);
            if (strategy == null) {
                LOGGER.warn("Timing strategy \"{}\" not found, using default instead.", timingStrategyName);
                strategy = DefaultTimingStrategy.INSTANCE;
            }
            return strategy.createTimer(config.filtered(strategy.name()));
        });
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public MetricRegistry getRegistry() {
        return registry;
    }
}
