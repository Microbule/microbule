package org.microbule.metrics.decorator;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.microbule.gson.spi.GsonCustomizer;

@Named("metricsGsonCustomizer")
@Singleton
public class MetricsGsonCustomizer implements GsonCustomizer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final double DURATION_FACTOR = 1.0 / TimeUnit.SECONDS.toNanos(1);

//----------------------------------------------------------------------------------------------------------------------
// GsonCustomizer Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void customize(GsonBuilder builder) {
        builder.registerTypeAdapter(Timer.class, new TimerSerializer());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class TimerSerializer implements JsonSerializer<Timer> {
//----------------------------------------------------------------------------------------------------------------------
// JsonSerializer Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public JsonElement serialize(Timer timer, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            final Snapshot snapshot = timer.getSnapshot();
            json.addProperty("count", timer.getCount());
            json.addProperty("max", snapshot.getMax() * DURATION_FACTOR);
            json.addProperty("mean", snapshot.getMean() * DURATION_FACTOR);
            json.addProperty("min", snapshot.getMin() * DURATION_FACTOR);

            json.addProperty("p50", snapshot.getMedian() * DURATION_FACTOR);
            json.addProperty("p75", snapshot.get75thPercentile() * DURATION_FACTOR);
            json.addProperty("p95", snapshot.get95thPercentile() * DURATION_FACTOR);
            json.addProperty("p98", snapshot.get98thPercentile() * DURATION_FACTOR);
            json.addProperty("p99", snapshot.get99thPercentile() * DURATION_FACTOR);
            json.addProperty("p999", snapshot.get999thPercentile() * DURATION_FACTOR);

            json.addProperty("stddev", snapshot.getStdDev() * DURATION_FACTOR);
            json.addProperty("m15_rate", timer.getFifteenMinuteRate());
            json.addProperty("m1_rate", timer.getOneMinuteRate());
            json.addProperty("m5_rate", timer.getFiveMinuteRate());
            json.addProperty("mean_rate", timer.getMeanRate());
            json.addProperty("duration_units", "seconds");
            json.addProperty("rate_units", "calls/second");
            return json;
        }
    }
}
