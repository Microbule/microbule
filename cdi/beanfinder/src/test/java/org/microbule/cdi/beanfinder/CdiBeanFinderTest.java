package org.microbule.cdi.beanfinder;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.microbule.test.cdi.CdiTestCase;

public class CdiBeanFinderTest extends CdiTestCase {

    @Inject
    private CdiBeanFinder finder;

    @Test
    public void testFind() {
        final List<HelloBean> hellos = finder.beanList(HelloBean.class);
        assertEquals(1, hellos.size());
    }
}