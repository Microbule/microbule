package org.microbule.util.jaxrs;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ExtensionRequestFilterTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private UriInfo uriInfo;

    @Captor
    private ArgumentCaptor<Response> responseCaptor;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetWithParam() throws Exception {
        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        final MultivaluedHashMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("_status", "bogus");
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);

        final StatusFilter filter = new StatusFilter();
        filter.filter(requestContext);
        verify(requestContext).getMethod();
        verify(requestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        verify(requestContext).abortWith(responseCaptor.capture());
        verifyNoMoreInteractions(requestContext, uriInfo);

        final Response response = responseCaptor.getValue();
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        assertEquals("ok", response.getEntity());
    }

    @Test
    public void testPostWithParam() throws Exception {
        when(requestContext.getMethod()).thenReturn("POST");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        final MultivaluedHashMap<String, String> queryParams = new MultivaluedHashMap<>();
        queryParams.add("_status", "bogus");
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);

        final StatusFilter filter = new StatusFilter();
        filter.filter(requestContext);
        verify(requestContext).getMethod();
        verifyNoMoreInteractions(requestContext, uriInfo);
    }

    @Test
    public void testGetWithoutParam() throws Exception {
        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        final MultivaluedHashMap<String, String> queryParams = new MultivaluedHashMap<>();
        when(uriInfo.getQueryParameters()).thenReturn(queryParams);

        final StatusFilter filter = new StatusFilter();
        filter.filter(requestContext);
        verify(requestContext).getMethod();
        verify(requestContext).getUriInfo();
        verify(uriInfo).getQueryParameters();
        verifyNoMoreInteractions(requestContext, uriInfo);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class StatusFilter extends ExtensionRequestFilter<String> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        @Override
        protected String createResponse(ContainerRequestContext requestContext) {
            return "ok";
        }

        @Override
        protected String markerParam() {
            return "_status";
        }
    }
}