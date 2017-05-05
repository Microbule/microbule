package org.microbule.test.server.hello;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("helloService")
public class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }

    @Override
    public String version() {
        return "1.0";
    }
}

