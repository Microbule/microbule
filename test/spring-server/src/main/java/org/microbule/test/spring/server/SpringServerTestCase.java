package org.microbule.test.spring.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.microbule.api.JaxrsProxyReference;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MicrobuleConfiguration.class)
@TestPropertySource("classpath:application.properties")
public abstract class SpringServerTestCase extends MockObjectTestCase {


    @Autowired
    private JaxrsProxyReference<HelloService> helloRef;

    @Test
    public void testSayHello() {
        assertEquals("Hello, Microbule!", helloRef.get().sayHello("Microbule"));
    }
}
