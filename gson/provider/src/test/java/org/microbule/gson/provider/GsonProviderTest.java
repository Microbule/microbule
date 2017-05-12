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

package org.microbule.gson.provider;

import org.junit.Test;
import org.microbule.config.api.Config;
import org.microbule.container.core.SimpleContainer;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.core.GsonServiceImpl;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;
import org.microbule.test.server.JaxrsServerTestCase;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public class GsonProviderTest extends JaxrsServerTestCase<PersonService> {

    @Mock
    private PersonService serviceImpl;

    @Override
    protected PersonService createImplementation() {
        return serviceImpl;
    }

    @Override
    protected void addBeans(SimpleContainer container) {
        GsonService gsonService = new GsonServiceImpl(container);
        container.addBean(new JaxrsServerDecorator() {
            @Override
            public String name() {
                return "gsonServer";
            }

            @Override
            public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
                descriptor.addProvider(new GsonProvider(gsonService));
            }
        });

        container.addBean(new JaxrsProxyDecorator() {
            @Override
            public String name() {
                return "gsonProxy";
            }

            @Override
            public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
                descriptor.addProvider(new GsonProvider(gsonService));
            }
        });
    }

    @Test
    public void testSerialization() {
        when(serviceImpl.findPerson("Slappy", "White")).thenReturn(new Person("Slappy", "White"));
        final Person person = createProxy().findPerson("Slappy", "White");
        assertEquals("Slappy", person.getFirstName());
        assertEquals("White", person.getLastName());
    }
}