package org.microbule.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.microbule.core.service.HelloService;

public class JaxrsServiceDescriptorImplTest extends Assert {

    private JaxrsServiceDescriptorImpl config;

    @Before
    public void initConfig() {
        config = new JaxrsServiceDescriptorImpl(HelloService.class);
    }

    @Test
    public void testServiceInterface() {
        assertEquals(HelloService.class, config.serviceInterface());
    }

}