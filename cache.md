# Cache-Control

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
