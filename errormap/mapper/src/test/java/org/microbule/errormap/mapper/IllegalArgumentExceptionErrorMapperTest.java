package org.microbule.errormap.mapper;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class IllegalArgumentExceptionErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final IllegalArgumentExceptionErrorMapper mapper = new IllegalArgumentExceptionErrorMapper();
        assertEquals(IllegalArgumentException.class, mapper.getExceptionType());
        assertEquals(Response.Status.BAD_REQUEST, mapper.getStatus());
    }
}