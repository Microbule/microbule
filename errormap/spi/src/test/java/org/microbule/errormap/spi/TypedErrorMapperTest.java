package org.microbule.errormap.spi;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.microbule.test.MockObjectTestCase;

public class TypedErrorMapperTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testMapping() {
        final ErrorMapper mapper = new SqlExceptionMapper();
        assertEquals(SQLException.class, mapper.getExceptionType());
        final SQLException exception = new SQLException("Oops!");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, mapper.getStatus(exception));
        assertEquals(Lists.newArrayList("Oops!"), mapper.getErrorMessages(exception));
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class SqlExceptionMapper extends TypedErrorMapper<SQLException> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public SqlExceptionMapper() {
            super(SQLException.class);
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        protected List<String> doGetErrorMessages(SQLException exception) {
            return Lists.newArrayList(exception.getMessage());
        }

        @Override
        protected Response.StatusType doGetStatus(SQLException exception) {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}