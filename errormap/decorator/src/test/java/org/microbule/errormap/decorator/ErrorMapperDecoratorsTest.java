package org.microbule.errormap.decorator;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.test.server.hello.HelloService;
import org.microbule.test.server.hello.HelloServiceImpl;
import org.microbule.test.server.hello.HelloTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ErrorMapperDecoratorsTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleBeanFinder finder) {
        when(errorMapperService.createResponse(any(Exception.class))).thenReturn(Response.serverError().build());
        when(errorMapperService.createException(any(Response.class))).thenReturn(new IllegalArgumentException("I'm not saying hello to you!"));
        finder.addBean(new ErrorMapperServerDecorator(errorMapperService));
        finder.addBean(new ErrorMapperProxyDecorator(errorMapperService));
    }

    @Override
    protected HelloService createImplementation() {
        return new EvilHelloServiceImpl();
    }

    @Test
    public void testWithException() {
        expectException(IllegalArgumentException.class, "I'm not saying hello to you!");
        createProxy().sayHello("Dr. Evil");
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class EvilHelloServiceImpl extends HelloServiceImpl {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public String sayHello(String name) {
            if("Dr. Evil".equals(name)) {
                throw new IllegalArgumentException("I'm not saying hello to you!");
            }
            return super.sayHello(name);
        }
    }
}