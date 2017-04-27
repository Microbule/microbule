package org.microbule.swagger.decorator;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.junit.Test;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

public class SwaggerDecoratorTest extends MockObjectTestCase {

    @Mock
    private JaxrsServiceDescriptor descriptor;

    @Captor
    private ArgumentCaptor<Swagger2Feature> captor;

    @Test
    public void testDecorate() {
        final SwaggerDecorator decorator = new SwaggerDecorator();
        decorator.decorate(descriptor, null);
        verify(descriptor).addFeature(captor.capture());
        final Swagger2Feature feature = captor.getValue();
        assertTrue(feature.isPrettyPrint());
    }

}