package org.microbule.osgi.core;

public class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}

