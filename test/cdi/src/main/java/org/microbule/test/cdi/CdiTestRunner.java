package org.microbule.test.cdi;

import javax.enterprise.inject.spi.CDI;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class CdiTestRunner  extends BlockJUnit4ClassRunner {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CdiTestRunner(Class<?> c) throws InitializationError {
        super(c);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Object createTest() throws Exception {
        SLF4JBridgeHandler.install();
        final WebBeansContext context = WebBeansContext.currentInstance();
        final ContainerLifecycle lifecycle = context.getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
        return CDI.current().select(getTestClass().getJavaClass()).get();
    }
}
