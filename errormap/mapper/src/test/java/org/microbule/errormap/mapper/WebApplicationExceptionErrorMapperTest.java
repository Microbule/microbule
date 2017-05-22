package org.microbule.errormap.mapper;

import java.util.Collections;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class WebApplicationExceptionErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetErrorMessages() {
        final WebApplicationExceptionErrorMapper errorMapper = new WebApplicationExceptionErrorMapper();
        assertEquals(Collections.singletonList("Not found!"), errorMapper.doGetErrorMessages(new NotFoundException("Not found!")));
        assertEquals(Collections.singletonList("Not Found"), errorMapper.doGetErrorMessages(new NotFoundException("")));
    }

    @Test
    public void testGetStatus() {
        final WebApplicationExceptionErrorMapper errorMapper = new WebApplicationExceptionErrorMapper();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), errorMapper.getStatus(new NotFoundException()).getStatusCode());
    }
}