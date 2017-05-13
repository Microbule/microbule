# Microbule


Microbule provides a framework for developing [Microservices](http://www.martinfowler.com/articles/microservices.html)
quickly and easily!  Services are written using the standard
 [Java API for RESTful Services](https://jax-rs-spec.java.net/) (JAX-RS).

## Getting Started

Microbule is an extension of the [Apache Karaf](http://karaf.apache.org) container.  Getting started with Microbule is
 simple:

1. [Download](http://karaf.apache.org/download.html) Apache Karaf (version 4.0.x) and extract to [KARAF_HOME].
2. Start Apache Karaf:

 ```
 cd [KARAF_HOME]
 bin/karaf
 ```

3. Install the Microbule Karaf [Feature](https://karaf.apache.org/manual/latest/provisioning):

 ```
         __ __                  ____
        / //_/____ __________ _/ __/
       / ,<  / __ `/ ___/ __ `/ /_
      / /| |/ /_/ / /  / /_/ / __/
     /_/ |_|\__,_/_/   \__,_/_/

   Apache Karaf (4.0.7)

 Hit '<tab>' for a list of available commands
 and '[cmd] --help' for help on a specific command.
 Hit '<ctrl-d>' or type 'system:shutdown' or 'logout' to shutdown Karaf.

 karaf@root()> repo-add mvn:org.microbule/microbule-features/0.1.0/xml/features
 karaf@root()> feature:install microbule
 ```

4. Install The Microbule Examples

 ```
 karaf@root()> feature:install microbule-examples
 ```

5. Enjoy!

## Writing Your Own Services

Microbule uses the OSGi [Whiteboard Pattern](http://enroute.osgi.org/doc/218-patterns.html) to discover JAX-RS services
at runtime.  In order to register a service with Microbule, you have to expose them as an OSGi service with the
"microbule.address" service property.  Microbule provides the following BundleActivator-based example:

```
public class HelloActivator implements BundleActivator {

    private ServiceRegistration<HelloResource> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        Dictionary<String,Object> props = new Hashtable<>();
        props.put("microbule.address", "/microbule-example-activator");
        registration = context.registerService(HelloResource.class, new DefaultHelloResource(), props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        registration.unregister();
    }
}

```

Microbule will automatically detect the service set its publish address as "/microbule-example-activator" using
[Apache CXF](http://cxf.apache.org) as the JAX-RS implementation.  Since Microbule uses OSGi services, you can use your
framework-of-choice (OSGi Blueprint, OSGi Declarative Services, CDI, etc.) to wire up your service implementation object.
The service *interface* should be annotated with all of the appropriate JAX-RS annotations.

## Microbule Features

Microbule provides many built-in features out-of-the-box.

### GSON

Microbule will transform payloads to/from JSON using the [Google GSON](https://github.com/google/gson) library.  For
example, given the following response type:

```
public class HelloResponse {

    private final String greeting;

    public HelloResponse(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }
}
```

Microbule will automatically format the response paylad as:

```
{
  "greeting": "Hello, Microbule!"
}
```

### Request Logging

Microbule will log begin/end events for every service call:

 ```
 2016-11-21 10:57:01,568  INFO RequestLogFilter - BEGIN GET hello/Microbule
 2016-11-21 10:57:01,583  INFO RequestLogFilter - END   GET hello/Microbule - 200 OK (0.012 sec)
 ```

### Bean Validation ([JSR-303](https://jcp.org/en/jsr/detail?id=303))

Microbule can automatically validate method parameters using Bean Validation:

 ```
 @Path("/")
 public interface HelloResource {

     @Path("/hello/{name}")
     @Produces(MediaType.APPLICATION_JSON)
     @GET
     @Cacheable
     HelloResponse sayHello(@PathParam("name") @Size(min = 5, message="Name must be at least 5 characters long.") String name);
 }
 ```

### Cross-Origin Resource Scripting (CORS)

Microbule automatically adds CORS headers to your responses.

### Tracer

Microbule will generate a the following random UUID header values automatically:

- **Microbule-Trace-ID**: a unique value corresponding to a "transaction."  If the header is found on the request, the
existing value will be used.
- **Microbule-Request-ID**: a unique value corresponding to the request itself.  This is generated for each and every
request.

A typical response will contain the following headers:

 ```
 Microbule-Request-ID=[3a5bf571-99fd-4c6b-b79e-7ff0a28e9350]
 Microbule-Trace-ID=[da3e59f4-8aff-4a89-bc30-7ad310fecf63]
 ```

### Cache-Control Header

Microbule will automatically manage the *Cache-Control* headers for you.  Each method annotated with @Cacheable will
yield a *Cache-Control* header.  For example, consider the following method:

 ```
 @GET
 @Produces(MediaType.TEXT_PLAIN)
 @Path("/value")
 @Cacheable(maxAge = 600)
 String getValue();
 ```

When executed, the *Cache-Control* header will contain:

 ```
 Cache-Control=[no-transform,max-age=600]
 ```

Microbule will also manage the *ETag* and *Last-Modified* headers for you.  You can provide these values by using a
JAX-RS Response object:

 ```
 public Response createResponse() {
   return Response.ok("payload").lastModified(new Date()).build();
 }
 ```

or by injecting a *ResourceState* object, using the @Context annotation:

  ```
  public class CacheResourceImpl implements CacheResource {

      @Context
      private ResourceState resourceState;

      @Override
      public String getValueWithEtag() {
          resourceState.setEntityTag("12345");
          return "payload";
      }
  }
  ```

When an *ETag* or *Last-Modified* value is provided, Microbule will check the *If-None-Match* and *If-Modified-Since*
headers correspondingly.  If the resource is up-to-date, Microbule will return a "No Content" (204) response.

### Client Timeout

Microbule will automatically set connection and receive timeouts (10 sec and 30 sec by default) on all generated client
proxies.  You can customize the timeout values using configuration properties when you create the proxy.  For example,
you can set the "microbule.timeout.receiveTimeout" property to a value (in milliseconds) to be used by all calls for
the proxy.  Alternatively, you can customize per-method (we use the method name as the key) timeouts by setting the
"microbule.timeout.foo.connectionTimeout" value, which would override the connection timeout value for the "foo" method
only.

### Circuit Breaker

All client proxies generated by Microbule will include a
[Circuit Breaker](http://martinfowler.com/bliki/CircuitBreaker.html) to avoid overloading services when they are
encountering issues.  By default, the circuit breaker will "open" when the service responds with more than *10* server
error level (500+) responses per second and will "close" when it drops back down below this threshold.

### Error Mapping

Microbule will inject its own JAX-RS ExceptionMappers into the services by default.  These ExceptionMappers will use
the ErrorMapperService to create responses when an exception happens.  The default format of the error response will be
a plain text response with newline-separated error messages.  You can override the default format by exposing an
ErrorResponseStrategy OSGi service.  To provide mapping for individual exception types, simply expose an ErrorMapper
OSGi service.

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

## What's in a Name?

A "microbule" is a unit of length used in Marvel's
[Guardians of the Galaxy](http://marvel.com/characters/70/guardians_of_the_galaxy) movie.  Microbule strives to be the
microservices framework that the others will try to measure up to!
