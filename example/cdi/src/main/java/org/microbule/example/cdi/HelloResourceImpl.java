package org.microbule.example.cdi;

import javax.inject.Inject;

import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.cdi.api.Properties;
import org.ops4j.pax.cdi.api.Property;
import org.slf4j.Logger;

@OsgiServiceProvider(classes = HelloResource.class)
@Properties(@Property(name = "microbule.address", value = "/microbule-example-cdi"))
public class HelloResourceImpl implements HelloResource {

    @Inject
    @QLogger
    private Logger logger;

    @Override
    public String sayHello(String name) {
        logger.info("Saying hello to {}...", name);
        return "Hello, " + name + "!";
    }
}
