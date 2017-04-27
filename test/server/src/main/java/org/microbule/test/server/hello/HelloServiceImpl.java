package org.microbule.test.server.hello;

public class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}

