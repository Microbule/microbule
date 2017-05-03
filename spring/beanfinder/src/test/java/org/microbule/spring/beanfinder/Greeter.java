package org.microbule.spring.beanfinder;

import java.util.Map;

import org.microbule.beanfinder.api.BeanFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Greeter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<String, HelloService> services;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Autowired
    public Greeter(BeanFinder finder) {
        this.services = finder.beanMap(HelloService.class, HelloService::lang);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public String sayHello(String lang, String name) {
        return services.get(lang).sayHello(name);
    }
}
