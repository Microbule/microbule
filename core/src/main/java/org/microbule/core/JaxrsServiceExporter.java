package org.microbule.core;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Path;

import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxrsServiceExporter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String MICROBULE_FILTER = "(microbule.address=*)";
    private static final String ADDRESS_PROP = "microbule.address";

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServiceExporter.class);

    private final BundleContext bundleContext;
    private final Map<Long, Server> servers = new ConcurrentHashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceExporter(BundleContext bundleContext) {
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
            e.printStackTrace();
        }
        try {
            ServiceReference<?>[] serviceReferences = bundleContext.getAllServiceReferences(null, MICROBULE_FILTER);
            if (serviceReferences != null) {
                Arrays.stream(serviceReferences).forEach(this::processService);
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void processService(ServiceReference<?> ref) {
        String[] serviceInterfaceNames = (String[]) ref.getProperty(Constants.OBJECTCLASS);
        Arrays.stream(serviceInterfaceNames).forEach(serviceInterfaceName -> {
            try {
                final Class<?> serviceInterface = ref.getBundle().loadClass(serviceInterfaceName);
                if (serviceInterface.isAnnotationPresent(Path.class)) {
                    final Long serviceId = serviceId(ref);
                    final String address = (String) ref.getProperty(ADDRESS_PROP);
                    LOGGER.info("Detected JAX-RS resource {} ({}) from bundle {} ({}).", serviceInterfaceName, serviceId, ref.getBundle().getSymbolicName(), ref.getBundle().getBundleId());
                    final Object serviceImplementation = bundleContext.getService(ref);
                    final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
                    sf.setBus(BusFactory.getDefaultBus(true));
                    sf.setServiceBean(serviceImplementation);
                    sf.setAddress(address);
                    Server server = sf.create();
                    servers.put(serviceId, server);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private Long serviceId(ServiceReference<?> ref) {
        return (Long) ref.getProperty(Constants.SERVICE_ID);
    }
}
