package org.microbule.osgi.blueprint;

import java.net.URL;
import java.util.Set;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.apache.aries.blueprint.mutable.MutableBeanMetadata;
import org.apache.aries.blueprint.mutable.MutableRefMetadata;
import org.microbule.osgi.beanfinder.OsgiBeanFinder;
import org.osgi.framework.BundleContext;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class MicrobuleNamespaceHandler implements NamespaceHandler {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String SCHEMA_LOCATION = "schemas/blueprint/microbule.xsd";
    private static final String ID_ATTR = "id";
    private static final String BUNDLE_CONTEXT_ID = "blueprintBundleContext";

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
        switch(element.getLocalName()) {
            case "beanFinder":
                return parseBeanFinder(element, parserContext);
        }
        return null;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Metadata parseBeanFinder(Element element, ParserContext parserContext) {
        final MutableBeanMetadata bean = parserContext.createMetadata(MutableBeanMetadata.class);
        bean.setClassName(OsgiBeanFinder.class.getName());
        bean.setRuntimeClass(OsgiBeanFinder.class);
        bean.setId(element.getAttribute(ID_ATTR));
        bean.addDependsOn(BUNDLE_CONTEXT_ID);
        bean.addArgument(bundleContextRef(parserContext), BundleContext.class.getName(), 0);
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
}
