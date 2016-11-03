package org.microbule.decorator.errormap;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.microbule.core.JaxrsProxyFactoryImpl;
import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.errormap.api.ErrorMapperService;
import org.microbule.test.hello.HelloService;
import org.microbule.test.hello.HelloServiceImpl;
import org.microbule.test.hello.HelloTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ErrorMapperServerDecoratorTest extends HelloTestCase{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private ErrorMapperService errorMapperService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        when(errorMapperService.createResponse(any(Exception.class))).thenReturn(Response.serverError().build());
        factory.addDecorator("errormap", new ErrorMapperServerDecorator(errorMapperService));


    }

    @Override
    protected void addDecorators(JaxrsProxyFactoryImpl factory) {
        when(errorMapperService.createException(any(Response.class))).thenReturn(new IllegalArgumentException("I'm not saying hello to you!"));
        factory.addDecorator("errormap", new ErrorMapperProxyDecorator(errorMapperService));
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