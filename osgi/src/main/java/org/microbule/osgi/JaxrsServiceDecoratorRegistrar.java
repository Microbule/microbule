package org.microbule.osgi;

import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.apache.commons.lang3.StringUtils;
import org.microbule.core.JaxrsServiceDecoratorRegistry;
import org.microbule.spi.JaxrsServiceDecorator;
import org.osgi.framework.BundleContext;

public abstract class JaxrsServiceDecoratorRegistrar<T extends JaxrsServiceDecorator> extends AbstractWhiteboard<T,String> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String NAME_PROP = "name";

    private final JaxrsServiceDecoratorRegistry<T> registry;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDecoratorRegistrar(BundleContext bundleContext, Class<T> serviceType, JaxrsServiceDecoratorRegistry<T> registry) {
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
