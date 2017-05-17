package org.microbule.spi;

import org.junit.Test;
import org.microbule.test.core.MicrobuleTestCase;

public class JaxrsAddressChooserTest extends MicrobuleTestCase {
    @Test
    public void testClose() {
        final JaxrsAddressChooser chooser = () -> "foo";
        chooser.close();
    }



}