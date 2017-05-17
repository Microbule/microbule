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
import java.util.function.Consumer;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.mutable.MutableBeanMetadata;
import org.apache.aries.blueprint.mutable.MutableRefMetadata;
import org.apache.aries.blueprint.mutable.MutableReferenceMetadata;
import org.apache.aries.blueprint.mutable.MutableServiceMetadata;
import org.apache.aries.blueprint.mutable.MutableValueMetadata;
import org.apache.commons.lang3.StringUtils;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.osgi.container.OsgiContainer;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MicrobuleNamespaceHandler implements NamespaceHandler {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String CONTAINER_ELEMENT = "container";
    public static final String QUIET_PERIOD_IN_MS_ATTR = "quietPeriodInMs";
    public static final String JAXRS_PROXY_FACTORY_ID = "_jaxrsProxyFactory";
    public static final String SERVICE_INTERFACE_ATTR = "serviceInterface";
    public static final String REF_ATTR = "ref";
    public static final String NAMESPACE_URI = "http://www.microbule.org/blueprint";
    public static final String CREATE_PROXY_METHOD = "createProxy";
    private static final Logger LOGGER = LoggerFactory.getLogger(MicrobuleNamespaceHandler.class);
    private static final String SCHEMA_LOCATION = "schemas/blueprint/microbule.xsd";
    private static final String ID_ATTR = "id";
    private static final String BUNDLE_CONTEXT_ID = "blueprintBundleContext";

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
    public URL getSchemaLocation(String namespaceUri) {
        if (NAMESPACE_URI.equals(namespaceUri)) {
            return getClass().getClassLoader().getResource(SCHEMA_LOCATION);
        }
        return null;
    }

    @Override
    public Metadata parse(Element element, ParserContext parserContext) {
        if (CONTAINER_ELEMENT.equals(element.getLocalName())) {
            return parseContainerElement(element, parserContext);
        } else if ("server".equals(element.getLocalName())) {
            return parseServerElement(element, parserContext);
        } else if ("proxy".equals(element.getLocalName())) {
            return parseProxyElement(element, parserContext);
        }
        return null;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private void createJaxrsProxyFactoryReference(ParserContext parserContext) {
        MutableReferenceMetadata jaxrsProxyFactory = serviceRef(parserContext, JAXRS_PROXY_FACTORY_ID, JaxrsProxyFactory.class);
        parserContext.getComponentDefinitionRegistry().registerComponentDefinition(jaxrsProxyFactory);
    }

    private MutableReferenceMetadata serviceRef(ParserContext parserContext, String id, Class<?> serviceInterface) {
        MutableReferenceMetadata ref = parserContext.createMetadata(MutableReferenceMetadata.class);
        ref.setId(id);
        ref.setRuntimeInterface(serviceInterface);
        return ref;
    }

    private Metadata parseContainerElement(Element element, ParserContext parserContext) {
        final MutableBeanMetadata bean = parserContext.createMetadata(MutableBeanMetadata.class);
        final String quietPeriodInMs = Optional.ofNullable(StringUtils.trimToNull(element.getAttribute(QUIET_PERIOD_IN_MS_ATTR))).orElse(String.valueOf(defaultQuietPeriodInMs));

        bean.setClassName(OsgiContainer.class.getName());
        bean.setRuntimeClass(OsgiContainer.class);
        bean.setId(element.getAttribute(ID_ATTR));
        bean.addDependsOn(BUNDLE_CONTEXT_ID);
        bean.addArgument(bundleContextRef(parserContext), BundleContext.class.getName(), 0);
        bean.addArgument(value(parserContext, quietPeriodInMs, Long.TYPE), Long.TYPE.getName(), 1);
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

    private <T> ValueMetadata value(ParserContext context, String value, Class<T> type) {
        final MutableValueMetadata metadata = context.createMetadata(MutableValueMetadata.class);
        metadata.setType(type.getName());
        metadata.setStringValue(value);
        return metadata;
    }

    public Metadata parseProxyElement(Element element, ParserContext parserContext) {
        whenNotExists(parserContext, JAXRS_PROXY_FACTORY_ID, this::createJaxrsProxyFactoryReference);

        MutableBeanMetadata bean = parserContext.createMetadata(MutableBeanMetadata.class);
        bean.setId(element.getAttribute(ID_ATTR));
        bean.addDependsOn(JAXRS_PROXY_FACTORY_ID);
        bean.setRuntimeClass(JaxrsProxyFactory.class);
        bean.setFactoryComponent(ref(parserContext, JAXRS_PROXY_FACTORY_ID));
        bean.setFactoryMethod(CREATE_PROXY_METHOD);
        bean.addArgument(value(parserContext, element.getAttribute(SERVICE_INTERFACE_ATTR), Class.class), Class.class.getName(), 0);
        LOGGER.info("Created proxy bean: {}", bean);
        return bean;
    }

    private void whenNotExists(ParserContext parserContext, String componentId, Consumer<ParserContext> fn) {
        if (!parserContext.getComponentDefinitionRegistry().containsComponentDefinition(componentId)) {
            fn.accept(parserContext);
        }
    }

    private Metadata parseServerElement(Element element, ParserContext parserContext) {
        final MutableServiceMetadata metadata = parserContext.createMetadata(MutableServiceMetadata.class);
        metadata.setId(parserContext.generateId());
        metadata.setServiceComponent(ref(parserContext, element.getAttribute(REF_ATTR)));
        metadata.addInterface(element.getAttribute(SERVICE_INTERFACE_ATTR));
        metadata.addServiceProperty(value(parserContext, "microbule.server", String.class), value(parserContext, "true", String.class));
        LOGGER.info("Created server service:{}", metadata);
        return metadata;
    }
}
