package org.microbule.errormap.impl;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.errormap.spi.ErrorMapper;
import org.microbule.test.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;

public class ErrorMapperServiceImplTest extends MockObjectTestCase {
    @Rule
    public final OsgiRule osgiRule = new OsgiRule();
    private ErrorMapperServiceImpl service;

    @Before
    public void initService() {
        service = new ErrorMapperServiceImpl(osgiRule.getBundleContext());
        osgiRule.registerService(ErrorMapper.class, new WebApplicationExceptionErrorMapper(), ServicePropsBuilder.props());
    }

    @Test
    public void testCreateResponse() {
        final Response response = service.createResponse(new NotFoundException("Didn't find it!"));
        assertEquals(404, response.getStatus());
        assertEquals("Didn't find it!", response.readEntity(String.class));
    }

    @Test
    public void testCreateResponseWithNoMapper() {
        final Response response = service.createResponse(new RuntimeException("Didn't find it!"));
        assertEquals(500, response.getStatus());
        assertEquals("Didn't find it!", response.readEntity(String.class));
    }

    @Test
    public void testCreateException() {
        final Exception exception = service.createException(Response.status(Response.Status.NOT_FOUND).build());
        assertEquals(NotFoundException.class, exception.getClass());
    }


}