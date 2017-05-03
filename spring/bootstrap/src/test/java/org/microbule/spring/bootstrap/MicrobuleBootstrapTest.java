package org.microbule.spring.bootstrap;

import org.microbule.api.JaxrsProxyFactory;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.ConfigService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MicrobuleBootstrapTest {
//----------------------------------------------------------------------------------------------------------------------
// main() method
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.microbule");
        ConfigService configService = context.getBean(ConfigService.class);
        System.setProperty("microbule.HelloService.server.serverAddress", "http://localhost:8080/hello");
        System.setProperty("microbule.HelloService.proxy.proxyAddress", "http://localhost:8080/hello");

        final JaxrsServerFactory factory = context.getBean(JaxrsServerFactory.class);

        final JaxrsServer server = factory.createJaxrsServer(HelloService.class, new HelloServiceImpl(), configService.getServerConfig(HelloService.class));

        JaxrsProxyFactory proxyFactory = context.getBean(JaxrsProxyFactory.class);

        final HelloService proxy = proxyFactory.createProxy(HelloService.class, configService.getProxyConfig(HelloService.class));
        System.out.println(proxy.sayHello("Microbule"));
        server.shutdown();
        System.exit(0);
    }
}