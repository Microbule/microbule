package org.microbule.errormap.mapper;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class IllegalArgumentExceptionMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final IllegalArgumentExceptionMapper mapper = new IllegalArgumentExceptionMapper();
        assertEquals(IllegalArgumentException.class, mapper.getExceptionType());
        assertEquals(Response.Status.BAD_REQUEST, mapper.getStatus());
    }
}