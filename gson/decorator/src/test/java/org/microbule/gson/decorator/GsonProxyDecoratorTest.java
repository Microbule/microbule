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

package org.microbule.gson.decorator;

import org.junit.Test;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.provider.GsonProvider;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class GsonProxyDecoratorTest extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Mock
    private GsonService gsonService;

    @Mock
    private JaxrsServiceDescriptor descriptor;

    @Test
    public void testDecorate() {
        final GsonProxyDecorator decorator = new GsonProxyDecorator(gsonService);
        assertEquals("gson", decorator.name());
        decorator.decorate(descriptor, null);
        verify(descriptor).addProvider(any(GsonProvider.class));
    }
}