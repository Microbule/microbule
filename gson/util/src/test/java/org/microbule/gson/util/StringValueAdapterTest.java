package org.microbule.gson.util;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class StringValueAdapterTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private Gson gson;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, StringValueAdapter.toStringAdapter(LocalDateTime::parse));
        gson = builder.serializeNulls().create();
    }

    @Test
    public void testReadingNulls() {
        final Holder holder = gson.fromJson("{\"now\":null}", Holder.class);
        assertNull(holder.now);
    }

    @Test
    public void testReadingValues() {
        final LocalDateTime now = LocalDateTime.now();
        final Holder holder = gson.fromJson(String.format("{\"now\":\"%s\"}", now.toString()), Holder.class);
        assertEquals(now, holder.now);
    }

    @Test
    public void testWritingNulls() {
        assertEquals("{\"now\":null}", gson.toJson(new Holder()));
    }

    @Test
    public void testWritingValues() {
        final LocalDateTime now = LocalDateTime.now();
        assertEquals(String.format("{\"now\":\"%s\"}", now.toString()), gson.toJson(new Holder(now)));
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class Holder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private LocalDateTime now;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public Holder() {
        }

        public Holder(LocalDateTime now) {
            this.now = now;
        }
    }
}