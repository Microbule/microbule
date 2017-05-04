package org.microbule.cdi.discovery;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;

public class CdiJaxrsServerDiscoveryTest {


    private static ContainerLifecycle lifecycle = null;
    public static void main(String[] args) {
        System.setProperty("microbule.HelloService.server.serverAddress", "http://localhost:8080/hello");
        lifecycle = WebBeansContext.currentInstance().getService(ContainerLifecycle.class);
        lifecycle.startApplication(null);
    }

    public static void shutdown() {
        lifecycle.stopApplication(null);
    }



}