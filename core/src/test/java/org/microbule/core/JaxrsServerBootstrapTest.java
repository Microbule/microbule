package org.microbule.core;

import org.junit.Test;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

public class JaxrsServerBootstrapTest extends MockObjectTestCase {

    @Mock
    private MicrobuleContainer container;

    @Mock
    private JaxrsServerFactory factory;

    @Test
    public void testToString() {
        final JaxrsServerBootstrap bootstrap = new JaxrsServerBootstrap(container, factory);
        assertEquals("JAX-RS Server Bootstrap", bootstrap.toString());
    }
}