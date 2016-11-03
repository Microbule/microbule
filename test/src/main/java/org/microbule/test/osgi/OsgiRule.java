package org.microbule.test.osgi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.connect.PojoServiceRegistryFactoryImpl;
import org.apache.felix.connect.launch.BundleDescriptor;
import org.apache.felix.connect.launch.ClasspathScanner;
import org.apache.felix.connect.launch.PojoServiceRegistry;
import org.apache.felix.connect.launch.PojoServiceRegistryFactory;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class OsgiRule implements TestRule {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private String bundleFilter = "(Bundle-SymbolicName=com.cengage.*)";
    private PojoServiceRegistry registry;

//----------------------------------------------------------------------------------------------------------------------
// TestRule Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Map<String, Object> config = new HashMap<>();
                ClasspathScanner scanner = new ClasspathScanner();
                List<BundleDescriptor> bundles = scanner.scanForBundles(bundleFilter);
                config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, bundles);
                registry = new PojoServiceRegistryFactoryImpl().newPojoServiceRegistry(config);
                statement.evaluate();
            }
        };
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public BundleContext getBundleContext() {
        return registry.getBundleContext();
    }

    public <S> S getService(ServiceReference<S> reference) {
        return registry.getService(reference);
    }

    public ServiceReference<?> getServiceReference(String clazz) {
        return registry.getServiceReference(clazz);
    }

    public ServiceReference<?>[] getServiceReferences(String clazz, String filter) throws InvalidSyntaxException {
        return registry.getServiceReferences(clazz, filter);
    }

    @SuppressWarnings("unchecked")
    public <T> ServiceRegistration<T> registerService(Class<? super T> serviceInterface, T serviceObject, ServicePropsBuilder builder) {
        return (ServiceRegistration<T>) registry.registerService(serviceInterface.getName(), serviceObject, builder.build());
    }

    public OsgiRule withBundleFilter(String bundleFilter) {
        this.bundleFilter = bundleFilter;
        return this;
    }
}
