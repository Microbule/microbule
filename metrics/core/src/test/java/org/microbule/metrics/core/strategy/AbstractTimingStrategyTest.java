package org.microbule.metrics.core.strategy;

import java.lang.reflect.Field;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Reservoir;
import com.codahale.metrics.Timer;
import org.microbule.test.core.MockObjectTestCase;

/**
 * Created by jcarman on 5/17/17.
 */
public class AbstractTimingStrategyTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    protected <T> T fieldValue(Object target, String fieldName) throws ReflectiveOperationException {
        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T)field.get(target);
    }

    protected <T extends Reservoir> T reservoir(Timer timer) throws ReflectiveOperationException{
        Histogram histogram = fieldValue(timer, "histogram");
        return fieldValue(histogram, "reservoir");
    }
}
