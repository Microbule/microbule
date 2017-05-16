package org.microbule.test.cdi.server;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.microbule.api.JaxrsProxyReference;
import org.microbule.test.cdi.CdiTestCase;
import org.microbule.test.core.hello.HelloService;

public abstract class CdiServerTestCase extends CdiTestCase {

    @Inject
    private JaxrsProxyReference<HelloService> helloRef;

    @BeforeClass
    public static void configure() {
        System.setProperty("HelloService.proxy.proxyAddress", "http://localhost:8080/HelloService");
        System.setProperty("default.server.baseAddress", "http://localhost:8080");
    }

    @Test
    public void testSayHello() {
        assertEquals("Hello, Microbule!", helloRef.get().sayHello("Microbule"));
    }
}
