package org.microbule.example.cdi;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

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

    @Context
    private UriInfo uriInfo;

    @Override
    public String sayHello(String name) {
        logger.info("Saying hello to '{}' on URI {}...", name, uriInfo.getAbsolutePath().getPath());
        return "Hello, " + name + "!";
    }
}
