package org.microbule.example.activator;

import java.util.Dictionary;
import java.util.Hashtable;

import org.microbule.example.common.DefaultHelloResource;
import org.microbule.example.common.HelloResource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class HelloActivator implements BundleActivator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private ServiceRegistration<HelloResource> registration;

//----------------------------------------------------------------------------------------------------------------------
// BundleActivator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void start(BundleContext context) throws Exception {
        Dictionary<String,Object> props = new Hashtable<>();
        props.put("microbule.address", "/microbule-example-activator");
        registration = context.registerService(HelloResource.class, new DefaultHelloResource(), props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        registration.unregister();
    }
}
