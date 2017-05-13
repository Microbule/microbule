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

package org.microbule.osgi.blueprint;

import java.net.URL;
import java.util.Optional;
import java.util.Set;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.mutable.MutableBeanMetadata;
import org.apache.aries.blueprint.mutable.MutableRefMetadata;
import org.apache.aries.blueprint.mutable.MutableValueMetadata;
import org.apache.commons.lang3.StringUtils;
import org.microbule.osgi.container.OsgiContainer;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MicrobuleNamespaceHandler implements NamespaceHandler {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String CONTAINER_ELEMENT = "container";
    private static final String SCHEMA_LOCATION = "schemas/blueprint/microbule.xsd";
    private static final String ID_ATTR = "id";
    private static final String BUNDLE_CONTEXT_ID = "blueprintBundleContext";
    public static final String QUIET_PERIOD_IN_MS_ATTR = "quietPeriodInMs";

    private final long defaultQuietPeriodInMs;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MicrobuleNamespaceHandler(long defaultQuietPeriodInMs) {
        this.defaultQuietPeriodInMs = defaultQuietPeriodInMs;
    }

//----------------------------------------------------------------------------------------------------------------------
// NamespaceHandler Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public ComponentMetadata decorate(Node node, ComponentMetadata componentMetadata, ParserContext parserContext) {
        return componentMetadata;
    }

    @Override
    public Set<Class> getManagedClasses() {
        return null;
    }

    @Override
    public URL getSchemaLocation(String s) {
        return getClass().getClassLoader().getResource(SCHEMA_LOCATION);
    }

    @Override
    public Metadata parse(Element element, ParserContext parserContext) {
        if (CONTAINER_ELEMENT.equals(element.getLocalName())) {
            return parseBeanFinder(element, parserContext);
        }
        return null;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Metadata parseBeanFinder(Element element, ParserContext parserContext) {
        final MutableBeanMetadata bean = parserContext.createMetadata(MutableBeanMetadata.class);
        final String attrValue = element.getAttribute(QUIET_PERIOD_IN_MS_ATTR);
        final long quietPeriodInMs = StringUtils.isNotEmpty(attrValue) ? Long.parseLong(attrValue) : defaultQuietPeriodInMs;
        bean.setClassName(OsgiContainer.class.getName());
        bean.setRuntimeClass(OsgiContainer.class);
        bean.setId(element.getAttribute(ID_ATTR));
        bean.addDependsOn(BUNDLE_CONTEXT_ID);
        bean.addArgument(bundleContextRef(parserContext), BundleContext.class.getName(), 0);
        bean.addArgument(value(parserContext, Long.TYPE, quietPeriodInMs), Long.TYPE.getName(), 1);
        return bean;
    }

    private RefMetadata bundleContextRef(ParserContext parserContext) {
        return ref(parserContext, BUNDLE_CONTEXT_ID);
    }

    private static RefMetadata ref(ParserContext context, String value) {
        MutableRefMetadata r = context.createMetadata(MutableRefMetadata.class);
        r.setComponentId(value);
        return r;
    }

    private <T> ValueMetadata value(ParserContext context, Class<T> type, T val) {
        final MutableValueMetadata metadata = context.createMetadata(MutableValueMetadata.class);
        metadata.setType(type.getName());
        metadata.setStringValue(Optional.ofNullable(val).map(Object::toString).orElse(null));
        return metadata;
    }
}
