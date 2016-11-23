package org.microbule.decorator.timeout;

public class DelayResourceImpl implements DelayResource {
//----------------------------------------------------------------------------------------------------------------------
// DelayResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String delay(long value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            // Do nothing!
        }
        return String.valueOf(value);
    }
}
