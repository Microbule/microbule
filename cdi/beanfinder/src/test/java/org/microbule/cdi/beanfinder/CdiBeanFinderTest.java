package org.microbule.cdi.beanfinder;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.microbule.test.cdi.CdiTestCase;

public class CdiBeanFinderTest extends CdiTestCase {

    @Inject
    private Greeter greeter;

    @Test
    public void testFind() {
        final List<HelloBean> hellos = greeter.getList();
        assertEquals(1, hellos.size());
    }
}