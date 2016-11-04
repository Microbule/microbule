package org.microbule.errormap.spi;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.MockObjectTestCase;

public class ConstantErrorMapperTest extends MockObjectTestCase{
    @Test
    public void testMapping() {
        final ConstantErrorMapper mapper = new ConstantErrorMapper(SQLException.class, Response.Status.INTERNAL_SERVER_ERROR);
        assertEquals(SQLException.class, mapper.getExceptionType());
        final SQLException exception = new SQLException("Oops!");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, mapper.getStatus(exception));
        assertEquals(Lists.newArrayList("Oops!"), mapper.getErrorMessages(exception));
    }

}