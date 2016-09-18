package org.microbule.example.hello;

public class HelloResourceImpl implements HelloResource {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
