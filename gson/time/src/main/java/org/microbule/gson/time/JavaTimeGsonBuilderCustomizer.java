package org.microbule.gson.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.gson.GsonBuilder;
import org.microbule.gson.spi.GsonBuilderCustomizer;
import org.microbule.gson.util.StringValueAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("javaTimeGsonBuilderCustomizer")
@Singleton
public class JavaTimeGsonBuilderCustomizer implements GsonBuilderCustomizer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaTimeGsonBuilderCustomizer.class);

//----------------------------------------------------------------------------------------------------------------------
// GsonBuilderCustomizer Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void customize(GsonBuilder builder) {
        LOGGER.info("Adding GSON type adapters for JavaSE 8 Date/Time classes...");
        builder.registerTypeAdapter(Duration.class, StringValueAdapter.toStringAdapter(Duration::parse));
        builder.registerTypeAdapter(Instant.class, StringValueAdapter.toStringAdapter(Instant::parse));
        builder.registerTypeAdapter(LocalDate.class, StringValueAdapter.toStringAdapter(LocalDate::parse));
        builder.registerTypeAdapter(LocalDateTime.class, StringValueAdapter.toStringAdapter(LocalDateTime::parse));
        builder.registerTypeAdapter(LocalTime.class, StringValueAdapter.toStringAdapter(LocalTime::parse));
        builder.registerTypeAdapter(MonthDay.class, StringValueAdapter.toStringAdapter(MonthDay::parse));
        builder.registerTypeAdapter(OffsetDateTime.class, StringValueAdapter.toStringAdapter(OffsetDateTime::parse));
        builder.registerTypeAdapter(OffsetTime.class, StringValueAdapter.toStringAdapter(OffsetTime::parse));
        builder.registerTypeAdapter(Period.class, StringValueAdapter.toStringAdapter(Period::parse));
        builder.registerTypeAdapter(Year.class, StringValueAdapter.toStringAdapter(Year::parse));
        builder.registerTypeAdapter(YearMonth.class, StringValueAdapter.toStringAdapter(YearMonth::parse));
    }
}
