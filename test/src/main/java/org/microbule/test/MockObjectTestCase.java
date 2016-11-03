package org.microbule.test;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class MockObjectTestCase extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
