package org.microbule.example.scr;

import org.microbule.example.common.AbstractHelloResource;
import org.microbule.example.common.HelloResource;
import org.osgi.service.component.annotations.Component;

@Component(property = "microbule.address=/microbule-example-scr", service = HelloResource.class)
public class HelloScr extends AbstractHelloResource {
}
