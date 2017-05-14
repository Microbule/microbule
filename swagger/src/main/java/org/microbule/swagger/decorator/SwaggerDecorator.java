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

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("swaggerDecorator")
public class SwaggerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setPrettyPrint(true);
        feature.setScan(false);
        descriptor.addFeature(feature);
    }

    @Override
    public String name() {
        return "swagger";
    }
}
