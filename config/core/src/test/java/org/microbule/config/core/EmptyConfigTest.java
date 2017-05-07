package org.microbule.config.core;

import org.junit.Assert;
import org.junit.Test;

public class EmptyConfigTest extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testValue() {
        assertFalse(EmptyConfig.INSTANCE.value("whatever").isPresent());
    }

    @Test
    public void testGroup() {
        assertSame(EmptyConfig.INSTANCE, EmptyConfig.INSTANCE.group("whatever"));
    }
}