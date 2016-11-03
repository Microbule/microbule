package org.microbule.core.osgi;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.apache.commons.lang3.StringUtils;
import org.microbule.core.JaxrsObjectDecoratorRegistry;
import org.microbule.spi.JaxrsObjectConfig;
import org.microbule.spi.JaxrsObjectDecorator;
import org.osgi.framework.BundleContext;

public abstract class JaxrsObjectDecoratorRegistrar<C extends JaxrsObjectConfig,T extends JaxrsObjectDecorator<C>> extends AbstractWhiteboard<T,String> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String NAME_PROP = "name";

    private final JaxrsObjectDecoratorRegistry<C,T> registry;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsObjectDecoratorRegistrar(BundleContext bundleContext, Class<T> serviceType, JaxrsObjectDecoratorRegistry<C,T> registry) {
        super(bundleContext, serviceType);
        this.registry = registry;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected String addService(T decorator, ServiceProperties serviceProperties) {
        final String name = serviceProperties.getProperty(NAME_PROP);
        if (StringUtils.isNotEmpty(name) && registry.addDecorator(name, decorator)) {
            return name;
        }
        return null;
    }

    @Override
    protected void removeService(T decorator, String name) {
        registry.removeDecorator(name);
    }
}
