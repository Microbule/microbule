/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
