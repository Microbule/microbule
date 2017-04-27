package org.microbule.errormap.impl;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class ResponsesTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetErrorMessage() {
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(), Responses.getErrorMessage(Response.serverError().build()));
    }
}