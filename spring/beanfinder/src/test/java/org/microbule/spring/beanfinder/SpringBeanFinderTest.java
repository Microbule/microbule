package org.microbule.spring.beanfinder;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBeanFinderTest extends MockObjectTestCase {

    @Test
    public void testFindingBeans() {
        final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext("org.microbule");

        Greeter greeter = ac.getBean(Greeter.class);

        assertEquals("Hello, Microbule!", greeter.sayHello("en", "Microbule"));
        assertEquals("Hola, Microbule!", greeter.sayHello("es", "Microbule"));


    }

}