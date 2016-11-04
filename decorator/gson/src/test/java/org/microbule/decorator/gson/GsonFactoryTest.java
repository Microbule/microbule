package org.microbule.decorator.gson;

import org.junit.Test;
import org.microbule.test.MockObjectTestCase;

public class GsonFactoryTest extends MockObjectTestCase {
    @Test
    public void testRemoveCustomizer() {
        final GsonFactory factory = new GsonFactory();
        final GsonCustomizer customizer = (builder) -> {
        };

        factory.removeCustomizer(customizer);
        factory.addCustomizer(customizer);
        factory.removeCustomizer(customizer);

    }
}