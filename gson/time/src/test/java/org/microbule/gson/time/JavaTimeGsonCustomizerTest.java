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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class JavaTimeGsonCustomizerTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private Gson gson;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initGson() {
        GsonBuilder builder = new GsonBuilder();
        new JavaTimeGsonCustomizer().customize(builder);
        gson = builder.serializeNulls().create();
    }

    @Test
    public void testJavaTypesSupported() {
        assertTypeSupported(Duration.ofMinutes(5));
        assertTypeSupported(Instant.now());
        assertTypeSupported(LocalDate.now());
        assertTypeSupported(LocalDateTime.now());
        assertTypeSupported(LocalTime.now());
        assertTypeSupported(MonthDay.now());
        assertTypeSupported(OffsetDateTime.now());
        assertTypeSupported(OffsetTime.now());
        assertTypeSupported(Period.ofWeeks(3));
        assertTypeSupported(Year.now());
        assertTypeSupported(YearMonth.now());
    }

    @SuppressWarnings("unchecked")
    private <T> void assertTypeSupported(T value) {
        final JsonPrimitive jsonValue = (JsonPrimitive)gson.toJsonTree(value);
        assertEquals(value.toString(), jsonValue.getAsString());
        T parsed = (T)gson.fromJson(jsonValue, value.getClass());
        assertEquals(value, parsed);
    }
}