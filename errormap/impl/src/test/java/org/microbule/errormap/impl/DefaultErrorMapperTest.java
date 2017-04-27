package org.microbule.errormap.impl;

import java.util.Collections;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;

public class DefaultErrorMapperTest extends MockObjectTestCase {
    @Test
    public void testGetErrorMessages() {
        assertEquals(Collections.singletonList("Oops!"), DefaultErrorMapper.INSTANCE.getErrorMessages(new RuntimeException("Oops!")));
    }

    @Test
    public void testGetExceptionType() {
        assertEquals(Exception.class, DefaultErrorMapper.INSTANCE.getExceptionType());
    }

    @Test
    public void testGetStatus() {
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, DefaultErrorMapper.INSTANCE.getStatus(new RuntimeException("Oops!")));
    }
}