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

package org.microbule.swagger.decorator;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.junit.Test;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.core.MockObjectTestCase;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

public class SwaggerDecoratorTest extends MockObjectTestCase {

    @Mock
    private JaxrsServiceDescriptor descriptor;

    @Captor
    private ArgumentCaptor<Swagger2Feature> captor;

    @Test
    public void testDecorate() {
        final SwaggerDecorator decorator = new SwaggerDecorator();
        decorator.decorate(descriptor, null);
        verify(descriptor).addFeature(captor.capture());
        final Swagger2Feature feature = captor.getValue();
        assertTrue(feature.isPrettyPrint());
    }

    @Test
    public void testName() {
        assertEquals("swagger", new SwaggerDecorator().name());
    }

}