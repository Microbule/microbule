# Apache Karaf

Microbule comes with a Karaf features repository file.  To install Microbule, simply execute the following commands in the Karaf console:

```
feature:repo-add mvn:org.microbule/microbule-features/[VERSION]/xml/features
feature:install microbule
```

# JAX-RS Service Discovery

Microbule uses the OSGi [Whiteboard Pattern](http://enroute.osgi.org/doc/218-patterns.html) to discover JAX-RS services at runtime.  In order to register a JAX-RS service with Microbule, you simply have to expose them as OSGi service with the "microbule.server" property (any value will do).  If you use a simple OSGi bundle activator:

```java
public class HelloActivator implements BundleActivator {

    private ServiceRegistration<HelloResource> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        Dictionary<String,Object> props = new Hashtable<>();
        props.put("microbule.server", "true");
        registration = context.registerService(HelloResource.class, new DefaultHelloResource(), props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        registration.unregister();
    }
}
```
