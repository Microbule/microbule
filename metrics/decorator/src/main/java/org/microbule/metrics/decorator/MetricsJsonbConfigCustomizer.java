package org.microbule.metrics.decorator;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.microbule.jsonb.spi.JsonbConfigCustomizer;

@Named("metricsGsonBuilderCustomizer")
@Singleton
public class MetricsJsonbConfigCustomizer implements JsonbConfigCustomizer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final double DURATION_FACTOR = 1.0 / TimeUnit.SECONDS.toNanos(1);

//----------------------------------------------------------------------------------------------------------------------
// JsonbConfigCustomizer Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void customize(JsonbConfig config) {
        config.withSerializers(new TimerSerializer());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class TimerSerializer implements JsonbSerializer<Timer> {
//----------------------------------------------------------------------------------------------------------------------
// JsonbSerializer Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public void serialize(Timer timer, JsonGenerator generator, SerializationContext ctx) {
            final Snapshot snapshot = timer.getSnapshot();
            generator.write("count", timer.getCount());
            generator.write("max", snapshot.getMax() * DURATION_FACTOR);
            generator.write("mean", snapshot.getMean() * DURATION_FACTOR);
            generator.write("min", snapshot.getMin() * DURATION_FACTOR);

            generator.write("p50", snapshot.getMedian() * DURATION_FACTOR);
            generator.write("p75", snapshot.get75thPercentile() * DURATION_FACTOR);
            generator.write("p95", snapshot.get95thPercentile() * DURATION_FACTOR);
            generator.write("p98", snapshot.get98thPercentile() * DURATION_FACTOR);
            generator.write("p99", snapshot.get99thPercentile() * DURATION_FACTOR);
            generator.write("p999", snapshot.get999thPercentile() * DURATION_FACTOR);

            generator.write("stddev", snapshot.getStdDev() * DURATION_FACTOR);
            generator.write("m15_rate", timer.getFifteenMinuteRate());
            generator.write("m1_rate", timer.getOneMinuteRate());
            generator.write("m5_rate", timer.getFiveMinuteRate());
            generator.write("mean_rate", timer.getMeanRate());
            generator.write("duration_units", "seconds");
            generator.write("rate_units", "calls/second");
        }
    }
}
