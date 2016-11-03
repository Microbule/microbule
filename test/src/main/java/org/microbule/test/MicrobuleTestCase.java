package org.microbule.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MicrobuleTestCase extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Rule
    public final ExpectedException exception = ExpectedException.none();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected void expectException(Class<? extends Exception> type, String msg, Object... params) {
        exception.expect(type);
        exception.expectMessage(String.format(msg, params));
    }
}
