package org.microbule.test.hello;

public class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}

