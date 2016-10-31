package org.microbule.core;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Path;

import com.google.common.collect.Lists;
import com.savoirtech.eos.pattern.whiteboard.KeyedWhiteboard;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxrsServerFactory extends KeyedWhiteboard<String, JaxrsServerDecorator> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENABLED_PROP_PATTERN = "microbule.%s.enabled";
    private static final String MICROBULE_FILTER = "(microbule.address=*)";
    private static final String ADDRESS_PROP = "microbule.address";

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServerFactory.class);

    private final BundleContext bundleContext;
    private final Map<Long, Server> servers = new ConcurrentHashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerFactory(BundleContext bundleContext) {
        super(bundleContext, JaxrsServerDecorator.class, (svc, props) -> props.getProperty("name"));
        this.bundleContext = bundleContext;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void destroy() {
        servers.values().forEach(Server::destroy);
    }

    public void init() {
        try {
            bundleContext.addServiceListener(event -> {
                switch (event.getType()) {
                    case ServiceEvent.REGISTERED:
                        processService(event.getServiceReference());
                        break;
                    case ServiceEvent.UNREGISTERING:
                        final Long serviceId = serviceId(event.getServiceReference());
                        final Server server = servers.get(serviceId);
                        if (server != null) {
                            server.destroy();
                        }
                }
            }, MICROBULE_FILTER);
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Unable to add service listener JAX-RS services using filter \"{}\".", MICROBULE_FILTER, e);
        }
        try {
            ServiceReference<?>[] serviceReferences = bundleContext.getAllServiceReferences(null, MICROBULE_FILTER);
            if (serviceReferences != null) {
                Arrays.stream(serviceReferences).forEach(this::processService);
            }
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Unable to search for JAX-RS services using filter \"{}\".", MICROBULE_FILTER, e);
        }
    }

    private void processService(ServiceReference<?> ref) {
        final String[] serviceInterfaceNames = (String[]) ref.getProperty(Constants.OBJECTCLASS);
        Arrays.stream(serviceInterfaceNames).forEach(serviceInterfaceName -> {
            try {
                final Class<?> serviceInterface = ref.getBundle().loadClass(serviceInterfaceName);
                if (serviceInterface.isAnnotationPresent(Path.class)) {
                    final Long serviceId = serviceId(ref);
                    final String address = (String) ref.getProperty(ADDRESS_PROP);
                    LOGGER.info("Detected JAX-RS resource {} ({}) at address {} from bundle {} ({}).", serviceInterfaceName, serviceId, address, ref.getBundle().getSymbolicName(), ref.getBundle().getBundleId());
                    final Object serviceImplementation = bundleContext.getService(ref);
                    final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
                    sf.setBus(BusFactory.getDefaultBus(true));
                    sf.setServiceBean(serviceImplementation);
                    sf.setAddress(address);
                    sf.setFeatures(Lists.newArrayList(new LoggingFeature(), createSwaggerFeature()));
                    final JaxrsServerImpl jaxrsServer = new JaxrsServerImpl(serviceInterface, ref);
                    final JaxrsServerPropertiesImpl serverProperties = new JaxrsServerPropertiesImpl(ref);
                    asMap().forEach((name, decorator) -> {
                        final String enabledProperty = String.format(ENABLED_PROP_PATTERN, name);
                        final Boolean enabled = serverProperties.getProperty(enabledProperty, Boolean::parseBoolean, true);
                        if (enabled) {
                            LOGGER.info("Decorating JAX-RS service using \"{}\" decorator...", name);
                            decorator.decorate(jaxrsServer, serverProperties);
                        }
                    });
                    sf.setProviders(jaxrsServer.getProviders());
                    servers.put(serviceId, sf.create());
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("Unable to create JAX-RS server!", e);
            }
        });
    }

    private Swagger2Feature createSwaggerFeature() {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setPrettyPrint(true);
        return feature;
    }

    private Long serviceId(ServiceReference<?> ref) {
        return (Long) ref.getProperty(Constants.SERVICE_ID);
    }
}
