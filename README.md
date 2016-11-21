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

 karaf@root()> repo-add mvn:org.microbule/microbule-features/<VERSION>/xml/features
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

Microbule will automatically detect the service set its publish address as "/microbule-example-activator"!  Since
Microbule only relies upon OSGi services, you can use your framework-of-choice
(OSGi Blueprint, OSGi Declarative Services, CDI, etc.) to wire up your service implementation object.

## Microbule Features

Microbule provides many built-in features out-of-the-box.

#### GSON

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

#### Request Logging

Microbule will log begin/end events for every service call:

 ```
 2016-11-21 10:57:01,568  INFO RequestLogFilter - BEGIN GET hello/Microbule
 2016-11-21 10:57:01,583  INFO RequestLogFilter - END   GET hello/Microbule - 200 OK (0.012 sec)
 ```

#### Bean Validation ([JSR-303](https://jcp.org/en/jsr/detail?id=303))

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

#### Cross-Origin Resource Scripting (CORS)

Microbule automatically adds CORS headers to your responses.

#### Tracer

Microbule will generate a the following random UUID header values automatically:

- **Microbule-Trace-ID**: a unique value corresponding to a "transaction."  If the header is found on the request, the
existing value will be used.
- **Microbule-Request-ID**: a unique value corresponding to the request itself.  This is generated for each and every
request.

#### Cache

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


## What's in a Name?

A "microbule" is a unit of length used in Marvel's
[Guardians of the Galaxy](http://marvel.com/characters/70/guardians_of_the_galaxy) movie.  Microbule strives to be the
microservices framework that the others will try to measure up to!