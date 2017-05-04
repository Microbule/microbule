package org.microbule.example.common;

import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("helloResource")
public class DefaultHelloResource implements HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

//----------------------------------------------------------------------------------------------------------------------
// HelloResource Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public HelloResponse sayHello(String name) {
        logger.info("Saying hello to {}...", name);
        return new HelloResponse("Hello, " + name + "!");
    }
}
