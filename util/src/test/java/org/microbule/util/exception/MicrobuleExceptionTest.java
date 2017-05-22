package org.microbule.util.exception;

import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class MicrobuleExceptionTest extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final MicrobuleException e = new MicrobuleException("One %s %s", "two", "three");
        assertEquals("One two three", e.getMessage());
    }

    @Test
    public void testConstructorWithCause() {
        final Throwable cause = new IllegalArgumentException("Not gonna do it!");
        final MicrobuleException e = new MicrobuleException(cause, "One %s %s", "two", "three");
        assertEquals("One, two, three", e.getMessage());
        assertEquals(cause, e.getCause());
    }
}