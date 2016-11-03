package org.microbule.test;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class MockObjectTestCase extends MicrobuleTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
