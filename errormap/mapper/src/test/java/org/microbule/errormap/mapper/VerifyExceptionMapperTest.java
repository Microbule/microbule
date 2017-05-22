package org.microbule.errormap.mapper;

import javax.ws.rs.core.Response;

import com.google.common.base.VerifyException;
import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class VerifyExceptionMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testConstructor() {
        final VerifyExceptionMapper mapper = new VerifyExceptionMapper();
        assertEquals(VerifyException.class, mapper.getExceptionType());
        assertEquals(Response.Status.BAD_REQUEST, mapper.getStatus());
    }
}