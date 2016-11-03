package org.microbule.decorator.gson.resource;

public class HelloResourceImpl implements HelloResource {

    @Override
    public HelloResponse sayHelloRaw(String name) {
        return new HelloResponse("Hello, " + name + "!");
    }

    @Override
    public HelloResponse sayHelloGeneric(String name) {
        return new HelloResponse("Hello, " + name + "!");
    }
}
