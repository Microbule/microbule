# Bean Validation

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
