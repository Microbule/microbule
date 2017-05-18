package org.microbule.metrics.decorator;

import java.util.HashMap;

import com.codahale.metrics.Timer;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class MetricsResponseTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final HashMap<String, Timer> timers = new HashMap<>();
        final MetricsResponse response = new MetricsResponse(timers);
        assertSame(timers, response.getTimers());
    }
}