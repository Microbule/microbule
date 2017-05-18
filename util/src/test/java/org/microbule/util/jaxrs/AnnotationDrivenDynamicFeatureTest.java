package org.microbule.util.jaxrs;

import javax.inject.Inject;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.junit.Test;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class AnnotationDrivenDynamicFeatureTest extends MockObjectTestCase {

    @Mock
    private ResourceInfo resourceInfo;

    @Mock
    private FeatureContext featureContext;

    @Test
    public void testConfigure() throws Exception  {
        when(resourceInfo.getResourceMethod()).thenReturn(TestResource.class.getMethod("resourceMethod"));

        final TestFeature feature = new TestFeature();
        feature.configure(resourceInfo, featureContext);
        assertTrue(feature.called);
    }


    public static class TestFeature extends AnnotationDrivenDynamicFeature<Inject> {
        private boolean called = false;
        @Override
        protected void configure(Inject annotation, ResourceInfo resourceInfo, FeatureContext featureContext) {
            called = true;
        }
    }

    public static class TestResource {

        @Inject
        public void resourceMethod() {

        }
    }
}