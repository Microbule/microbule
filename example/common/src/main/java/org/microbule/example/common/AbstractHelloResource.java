package org.microbule.example.common;

public abstract class AbstractHelloResource implements HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// HelloResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public HelloResponse sayHello(String name) {
        return new HelloResponse("Hello, " + name + "!");
    }
}
