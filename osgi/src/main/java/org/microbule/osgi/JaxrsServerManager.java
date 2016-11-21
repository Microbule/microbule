package org.microbule.osgi;

import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.ws.rs.Path;

import com.google.common.collect.MapMaker;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxrsServerManager {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String ADDRESS_PROP = "microbule.address";
    private static final String MICROBULE_FILTER = "(microbule.address=*)";


    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServerManager.class);

    private final BundleContext bundleContext;

    private final JaxrsServerFactory factory;
    private final Map<Long, JaxrsServer> servers = new MapMaker().makeMap();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerManager(BundleContext bundleContext, JaxrsServerFactory factory, long quietPeriod) {
        this.bundleContext = bundleContext;
        this.factory = factory;
        new BootstrapTask(quietPeriod).schedule();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void destroy() {
        servers.values().forEach(JaxrsServer::shutdown);
    }

    private void findExistingServices() {
        try {
            LOGGER.info("Searching for existing JAX-RS services (filter=\"{}\")...", MICROBULE_FILTER);
            ServiceReference<?>[] serviceReferences = bundleContext.getAllServiceReferences(null, MICROBULE_FILTER);
            if (serviceReferences != null) {
                Arrays.stream(serviceReferences).forEach(JaxrsServerManager.this::processService);
            }
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Unable to search for JAX-RS services using filter \"{}\".", MICROBULE_FILTER, e);
        }
    }

    private void registerServiceListener() {
        try {
            LOGGER.info("Registering ServiceListener to search for JAX-RS services (filter=\"{}\")...", MICROBULE_FILTER);
            bundleContext.addServiceListener(event -> {
                switch (event.getType()) {
                    case ServiceEvent.REGISTERED:
                        processService(event.getServiceReference());
                        break;
                    case ServiceEvent.UNREGISTERING:
                        final Long serviceId = serviceId(event.getServiceReference());
                        final JaxrsServer server = servers.get(serviceId);
                        if (server != null) {
                            server.shutdown();
                        }
                }
            }, MICROBULE_FILTER);
        } catch (InvalidSyntaxException e) {
            LOGGER.error("Unable to add service listener JAX-RS services using filter \"{}\".", MICROBULE_FILTER, e);
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
                    LOGGER.info("Detected JAX-RS service {} ({}) at address {} from bundle {} ({}).", serviceInterfaceName, serviceId, address, ref.getBundle().getSymbolicName(), ref.getBundle().getBundleId());
                    final Object serviceImplementation = bundleContext.getService(ref);
                    JaxrsServer server = factory.createJaxrsServer(serviceInterface, serviceImplementation, address, toProperties(ref));
                    servers.put(serviceId, server);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("Unable to create JAX-RS server!", e);
            }
        });
    }

    private static Map<String, Object> toProperties(ServiceReference<?> ref) {
        return Arrays.stream(ref.getPropertyKeys()).collect(Collectors.toMap(key -> key, ref::getProperty));
    }

    private Long serviceId(ServiceReference<?> ref) {
        return (Long) ref.getProperty(Constants.SERVICE_ID);
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private class BootstrapTask extends TimerTask {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final long quietPeriod;
        private Timer timer;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public BootstrapTask(long quietPeriod) {
            this.quietPeriod = quietPeriod;
        }

//----------------------------------------------------------------------------------------------------------------------
// Runnable Implementation
//----------------------------------------------------------------------------------------------------------------------

        @Override
        public void run() {
            LOGGER.info("Quiet period ({} ms) expired, executing Microbule Bootstrap...", quietPeriod);
            registerServiceListener();
            findExistingServices();
            LOGGER.info("Canceling Microbule Bootstrap timer thread...");
            timer.cancel();
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        public void schedule() {
            LOGGER.info("Scheduling Microbule Bootstrap task to run after quiet period ({} ms)...", quietPeriod);
            this.timer = new Timer("Microbule Bootstrap Timer");
            timer.schedule(this, quietPeriod);
        }
    }
}
