# Microbule


Microbule provides a framework for developing [Microservices](http://www.martinfowler.com/articles/microservices.html)
quickly and easily!  Services are written using the standard
 [Java API for RESTful Services](https://jax-rs-spec.java.net/) (JAX-RS).

## Table of Contents


- [Getting Started](#getting-started)
- [Features](#microbule-features)
- [What's in the Box](#whats-in-the-box)

## Getting Started

Microbule supports many of the popular deployment containers/frameworks available.  Simply choose from:

- [Spring Framework](spring.md)
- [Contexts and Dependency Injection (CDI)](cdi.md)
- [Apache Karaf](karaf.md)

## What's in the Box?

Okay, so maybe Microbule doesn't really come in a box, but it does include some very helpful features "out-of-the-box":

- [JSON Binding](json.md)
- [Circuit Breaker](circuitbreaker.md)
- [Request Logging](requestlog.md)
- [Bean Validation](validation.md)
- [Cross-Origin Resource Scripting (CORS)](cors.md)
- [Tracer](tracer.md)
- [Cache-Control](cache.md)
- [Connect/Receive Timeouts](timeout.md)
- [Error Mapping](errormap.md)
- [GZIP](gzip.md)
- [Swagger](swagger.md)
- [Metrics](metrics.md)

## Creating Client Proxies

One of the beautiful features of Apache CXF is its ability to generate dynamic client proxies that implement the JAX-RS
service interface.  Microbule uses this feature to provide type-safe client proxies:

```
JaxrsProxyFactory proxyFactory = ...;
Map<String,Object> props = new HashMap<>();
props.put("microbule.circuitbreaker.enabled", "false");
HelloService helloService = proxyFactory.createProxy(HelloService.class, "http://localhost:8383/HelloService", props);
```

Microbule exposes a JaxrsProxyFactory OSGi service for you to use.  Simply inject it wherever you need to create client
proxies.

## Extending Microbule

Extending Microbule couldn't be simpler.  JAX-RS relies upon the notion of "providers" in order to customize the
"containers" and "clients."  Microbule provides two extension points JaxrsServerDecorator and JaxrsProxyDecorator which
allow you to add container and client providers, respectively:

 ```
 public class MyServerDecorator implements JaxrsServerDecorator {
     @Override
     public void decorate(JaxrsServerConfig server) {
         MyServerProvider provider = new MyServerProvider();
         server.addProvider(provider);
     }
   }
 }
 ```

The JaxrsServerConfig object will provide access to the service interface, the base address, and the service properties
associated with the OSGi service used for the server.  To register your provider, you must expose it as an OSGi service
with the "name" service property indicating its name.  Similarly, for customizing client proxies, you simply implement
JaxrsProxyDecorator:

 ```
 public class MyProxyDecorator implements JaxrsProxyDecorator {
     @Override
     public void decorate(JaxrsProxyConfig proxy) {
         MyProxyProvider provider = new MyProxyProvider();
         proxy.addProvider(provider);
     }
 }
 ```

Again, the JaxrsProxyDecorator must be expopsed as an OSGi service with the "name" property.

## Opting Out

By default, Microbule will apply all registered decorators to your services/proxies.  However, you can opt out of them
individually using a service property.  For example, if you want to provide your own JSON processing provider, you can
disable the GSON-based provider by setting the following service property:

 ```
 microbule.gson.enabled=false
 ```
