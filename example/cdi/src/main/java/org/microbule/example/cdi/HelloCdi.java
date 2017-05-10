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

package org.microbule.example.cdi;

import javax.inject.Inject;

import org.microbule.example.common.DefaultHelloResource;
import org.microbule.example.common.HelloResource;
import org.microbule.example.common.HelloResponse;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
import org.slf4j.Logger;

@OsgiServiceProvider(classes = HelloResource.class)
@Properties({
        @Property(name = "microbule.server", value = "true")
})
public class HelloCdi extends DefaultHelloResource implements HelloResource {

    @Inject
    @QLogger
    private Logger logger;

    @Override
    public HelloResponse sayHello(String name) {
        logger.info("Saying hello to \"{}\".", name);
        return super.sayHello(name);
    }
}
