package org.microbule.decorator.gson;

import com.google.gson.GsonBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.microbule.test.MockObjectTestCase;
import org.microbule.test.osgi.OsgiRule;
import org.microbule.test.osgi.ServicePropsBuilder;
import org.mockito.Mock;
import org.osgi.framework.ServiceRegistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


public class GsonCustomizerRegistrarTest extends MockObjectTestCase {
    @Rule
    public final OsgiRule osgiRule = new OsgiRule();

    @Mock
    private GsonCustomizer gsonCustomizer;

    @Test
    public void testAddingCustomizer() {
        final GsonFactory factory = new GsonFactory();
        new GsonCustomizerRegistrar(osgiRule.getBundleContext(), factory);
        final ServiceRegistration<GsonCustomizer> registration = osgiRule.registerService(GsonCustomizer.class, gsonCustomizer, new ServicePropsBuilder());
        verify(gsonCustomizer).customize(any(GsonBuilder.class));
        registration.unregister();
    }

}