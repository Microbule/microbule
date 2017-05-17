package org.microbule.osgi.blueprint;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.common.collect.Lists;
import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.mutable.MutableBeanMetadata;
import org.apache.aries.blueprint.mutable.MutableRefMetadata;
import org.apache.aries.blueprint.mutable.MutableReferenceMetadata;
import org.apache.aries.blueprint.mutable.MutableServiceMetadata;
import org.apache.aries.blueprint.mutable.MutableValueMetadata;
import org.apache.aries.blueprint.reflect.BeanMetadataImpl;
import org.apache.aries.blueprint.reflect.RefMetadataImpl;
import org.apache.aries.blueprint.reflect.ReferenceMetadataImpl;
import org.apache.aries.blueprint.reflect.ServiceMetadataImpl;
import org.apache.aries.blueprint.reflect.ValueMetadataImpl;
import org.junit.Before;
import org.junit.Test;
import org.microbule.osgi.container.OsgiContainer;
import org.microbule.test.core.MockObjectTestCase;
import org.microbule.test.core.hello.HelloService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.reflect.BeanArgument;
import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.MapEntry;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ReferenceMetadata;
import org.osgi.service.blueprint.reflect.ServiceMetadata;
import org.osgi.service.blueprint.reflect.Target;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.microbule.osgi.blueprint.MicrobuleNamespaceHandler.NAMESPACE_URI;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MicrobuleNamespaceHandlerTest extends MockObjectTestCase {
    public static final int QUIET_PERIOD_IN_MS = 3000;
    @Mock
    private ParserContext parserContext;

    @Mock
    private ComponentDefinitionRegistry componentDefinitionRegistry;

    @Captor
    private ArgumentCaptor<ReferenceMetadata> jaxrsProxyFactoryCaptor;

    @Mock
    private ComponentMetadata metadata;
    private MicrobuleNamespaceHandler handler;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Before
    public void initHandler() {
        handler = new MicrobuleNamespaceHandler(QUIET_PERIOD_IN_MS);
    }

    protected Element parseRootElement() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().parse(getClass().getResourceAsStream("/blueprint/example-blueprint.xml"));
        return doc.getDocumentElement();
    }

    @Test
    public void testDecorate() {
        assertEquals(metadata, handler.decorate(null, metadata, parserContext));
    }

    @Test
    public void testGetManagedClasses() {
        assertNull(handler.getManagedClasses());
    }

    @Test
    public void testGetSchemaLocation() {
        assertEquals(getClass().getClassLoader().getResource("schemas/blueprint/microbule.xsd"), handler.getSchemaLocation(MicrobuleNamespaceHandler.NAMESPACE_URI));
        assertNull(handler.getSchemaLocation("foo"));
    }

    @Test
    public void testParseUnknown() throws Exception {
        Element root = parseRootElement();
        NodeList nodes = root.getElementsByTagNameNS("http://www.osgi.org/xmlns/blueprint/v1.0.0", "bean");
        Element element = (Element) nodes.item(0);
        assertNull(handler.parse(element, parserContext));
    }
        @Test
    public void testParseContainer() throws Exception {
        Element root = parseRootElement();
        NodeList nodes = root.getElementsByTagNameNS(NAMESPACE_URI, "container");
        Element element = (Element) nodes.item(0);

        Metadata parsed = handler.parse(element, parserContext);
        assertTrue(parsed instanceof BeanMetadata);
        BeanMetadata beanMetadata= (BeanMetadata)parsed;

        assertEquals(OsgiContainer.class.getName(), beanMetadata.getClassName());
        assertEquals(2, beanMetadata.getArguments().size());
        final BeanArgument argument1 = beanMetadata.getArguments().get(0);
        assertTrue(argument1.getValue() instanceof RefMetadata);
        RefMetadata bundleContextRef = (RefMetadata)argument1.getValue();
        assertEquals("blueprintBundleContext", bundleContextRef.getComponentId());
        assertEquals(BundleContext.class.getName(), argument1.getValueType());

        final BeanArgument argument2 = beanMetadata.getArguments().get(1);
        assertTrue(argument2.getValue() instanceof ValueMetadata);
        ValueMetadata quietPeriodInMs = (ValueMetadata)argument2.getValue();
        assertEquals("1000", quietPeriodInMs.getStringValue());
        assertEquals(Long.TYPE.getName(), quietPeriodInMs.getType());
        assertEquals(Long.TYPE.getName(), argument2.getValueType());
    }

    @Test
    public void testParseServer() throws Exception {
        Element root = parseRootElement();
        NodeList nodes = root.getElementsByTagNameNS(NAMESPACE_URI, "server");
        Element element = (Element) nodes.item(0);

        Metadata parsed = handler.parse(element, parserContext);

        assertTrue(parsed instanceof ServiceMetadata);
        ServiceMetadata serviceMetadata = (ServiceMetadata)parsed;
        assertEquals(1, serviceMetadata.getServiceProperties().size());

        final MapEntry entry = serviceMetadata.getServiceProperties().get(0);
        final ValueMetadata key = (ValueMetadata) entry.getKey();
        assertEquals("microbule.server", key.getStringValue());
        assertEquals(String.class.getName(), key.getType());

        final ValueMetadata value = (ValueMetadata) entry.getValue();
        assertEquals("true", value.getStringValue());
        assertEquals(String.class.getName(), value.getType());

        assertEquals(Lists.newArrayList(HelloService.class.getName()), serviceMetadata.getInterfaces());
        final Target target = serviceMetadata.getServiceComponent();
        assertTrue(target instanceof  RefMetadata);
        RefMetadata targetRef = (RefMetadata)target;
        assertEquals("helloService", targetRef.getComponentId());
    }

    @Test
    public void testParseProxy() throws Exception {
        Element root = parseRootElement();
        NodeList nodes = root.getElementsByTagNameNS(NAMESPACE_URI, "proxy");
        Element element = (Element) nodes.item(0);

        Metadata parsed = handler.parse(element, parserContext);

        assertTrue(parsed instanceof BeanMetadata);
        BeanMetadata beanMetadata = (BeanMetadata) parsed;
        assertEquals("createProxy", beanMetadata.getFactoryMethod());
        assertTrue(beanMetadata.getFactoryComponent() instanceof RefMetadata);

        RefMetadata omnibusFactoryRef = (RefMetadata) beanMetadata.getFactoryComponent();
        assertEquals(MicrobuleNamespaceHandler.JAXRS_PROXY_FACTORY_ID, omnibusFactoryRef.getComponentId());
        assertEquals(Collections.singletonList(MicrobuleNamespaceHandler.JAXRS_PROXY_FACTORY_ID), beanMetadata.getDependsOn());
        verify(parserContext, atLeastOnce()).getComponentDefinitionRegistry();
        verify(componentDefinitionRegistry).containsComponentDefinition(MicrobuleNamespaceHandler.JAXRS_PROXY_FACTORY_ID);
        verify(componentDefinitionRegistry).registerComponentDefinition(jaxrsProxyFactoryCaptor.capture());
    }

//    @Test
//    public void testParseProducerWhenOmnibusExists() throws Exception {
//        Element root = parseRootElement();
//
//        OmnibusNamespaceHandler handler = new OmnibusNamespaceHandler();
//        NodeList nodes = root.getElementsByTagNameNS("http://www.cengage.com/omnibus", "producer");
//        Element element = (Element) nodes.item(0);
//
//        when(componentDefinitionRegistry.containsComponentDefinition("_omnibus")).thenReturn(true);
//        Metadata parsed = handler.parse(element, parserContext);
//
//
//        assertTrue(parsed instanceof BeanMetadata);
//        BeanMetadata beanMetadata = (BeanMetadata) parsed;
//        assertEquals("_stopHeartBeat", beanMetadata.getDestroyMethod());
//        assertEquals("_startHeartBeat", beanMetadata.getInitMethod());
//        assertEquals("createProducer", beanMetadata.getFactoryMethod());
//        assertTrue(beanMetadata.getFactoryComponent() instanceof RefMetadata);
//
//        RefMetadata omnibusFactoryRef = (RefMetadata) beanMetadata.getFactoryComponent();
//        assertEquals("_omnibus", omnibusFactoryRef.getComponentId());
//
//        assertEquals(Arrays.asList("blueprintBundleContext", "_omnibus"), beanMetadata.getDependsOn());
//        verify(parserContext, atLeastOnce()).getComponentDefinitionRegistry();
//        verify(componentDefinitionRegistry).containsComponentDefinition("_omnibus");
//    }
//
//    @Test
//    public void testParseUnknown() throws Exception {
//        Element root = parseRootElement();
//
//        assertNull(new OmnibusNamespaceHandler().parse((Element) root.getElementsByTagName("bean").item(0), parserContext));
//    }

    @Before
    public void trainParserContext() {
        when(parserContext.getComponentDefinitionRegistry()).thenReturn(componentDefinitionRegistry);
        when(parserContext.createMetadata(MutableReferenceMetadata.class)).thenAnswer(invocation -> new ReferenceMetadataImpl());
        when(parserContext.createMetadata(MutableBeanMetadata.class)).thenAnswer(invocation -> new BeanMetadataImpl());
        when(parserContext.createMetadata(MutableRefMetadata.class)).thenAnswer(invocation -> new RefMetadataImpl());
        when(parserContext.createMetadata(MutableValueMetadata.class)).thenAnswer(invocation -> new ValueMetadataImpl());
        when(parserContext.createMetadata(MutableServiceMetadata.class)).thenAnswer(invocation -> new ServiceMetadataImpl());
    }
}