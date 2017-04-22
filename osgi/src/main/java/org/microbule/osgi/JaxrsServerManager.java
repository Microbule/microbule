package org.microbule.osgi;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Path;

import com.google.common.collect.MapMaker;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.MapConfig;
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

    public static final String MICROBULE_GROUP = "microbule";
    private static final String MICROBULE_FILTER = "(microbule.server=*)";


    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServerManager.class);

    private final BundleContext bundleContext;

    private final JaxrsServerFactory factory;
    private final Map<Long, JaxrsServer> servers = new MapMaker().makeMap();
    private final AtomicLong lastUpdated = new AtomicLong(System.nanoTime());
    private final ConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    private static Map<String, String> toMap(ServiceReference<?> ref) {
        final Map<String, String> map = Stream.of(ref.getPropertyKeys()).collect(Collectors.toMap(key -> key, key -> Optional.ofNullable(ref.getProperty(key)).map(String::valueOf).orElse(null)));
        LOGGER.info("Mapped {} to map {}.", ref, map);
        return map;
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServerManager(BundleContext bundleContext, ConfigService configService, JaxrsServerFactory factory, long quietPeriod) {
        this.bundleContext = bundleContext;
        this.configService = configService;
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
                            bundleContext.ungetService(event.getServiceReference());
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
                    lastUpdated.set(System.nanoTime());
                    final Long serviceId = serviceId(ref);
                    final Config serviceConfig = new MapConfig(toMap(ref)).group(MICROBULE_GROUP);
                    final Config config = configService.getServerConfig(serviceInterface, serviceConfig);
                    final String address = config.value(JaxrsServerFactory.ADDRESS_PROP).orElse(null);
                    LOGGER.info("Detected JAX-RS service {} ({}) at address {} from bundle {} ({}).", serviceInterfaceName, serviceId, address, ref.getBundle().getSymbolicName(), ref.getBundle().getBundleId());
                    final Object serviceImplementation = bundleContext.getService(ref);
                    JaxrsServer server = factory.createJaxrsServer(serviceInterface, serviceImplementation, config);
                    servers.put(serviceId, server);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("Unable to create JAX-RS server!", e);
            }
        });
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
        private ScheduledExecutorService scheduler;
        private final AtomicBoolean bootstrapped = new AtomicBoolean(false);

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
            if (!bootstrapped.get() && System.nanoTime() - lastUpdated.get() > quietPeriod * 1000000) {
                bootstrapped.set(true);
                LOGGER.info("Shutting down Microbule Bootstrap scheduler thread...");
                scheduler.shutdown();
                LOGGER.info("Quiet period ({} ms) expired, executing Microbule Bootstrap...", quietPeriod);
                registerServiceListener();
                findExistingServices();
            }
        }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

        public void schedule() {
            if (quietPeriod > 0) {
                scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleWithFixedDelay(this::run, quietPeriod, 1000, TimeUnit.MILLISECONDS);
            } else {
                LOGGER.info("Executing Microbule Bootstrap...");
                registerServiceListener();
                findExistingServices();
            }
        }
    }
}
