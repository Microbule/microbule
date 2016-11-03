package org.microbule.core.service;

public class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}

