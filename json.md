# JSON Binding

Microbule includes JSON binding support using the [Google GSON](https://github.com/google/gson) library. For example, consider the following JAX-RS service: 

```java
@Path("/")
public interface PersonService {

    @GET
    @Path("/people/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Person getPerson(@PathParam("id") String id);

    @POST
    @Path("/people")
    @Consumes(MediaType.APPLICATION_JSON)
    void addPerson(Person person);
}
```

which uses the *Person* class:

```java
public class Person {

    private final String id;
    private String firstName;
    private String lastName;

    public Person(String id) {
        this.id = id;
    }
    
    // Getters/Setters...
}
```

Microbule allows you to implement your service using this simle Java-based API:

```java
public class PersonServiceImpl implements PersonService {
  
  private final PersonDao dao;
  
  public PersonServiceImpl(PersonDao dao) {
    this.dao = dao;
  }
  
  public Person getPerson(String id) {
    return dao.findById(id);
  }
  
  public void addPerson(Person person) {
    dao.save(person);
  }
}
```

Furthermore, clients using your service can use Microbule-generated type-safe proxies and simply code to your service interface.  Microbule will take care of all of the JSON serialization/deserialization automatically.  When calling the *addPerson()* method, for instance, Microbule will generate the following JSON request body:

```json
{
  "id": "12345",
  "firstName": "Mr.",
  "lastName": "Microbule"
}
```

## Customizing GSON

By default, GSON can do a pretty good job transforming your objects to/from JSON.  There are some times, however, when you will want to customize how GSON handles certain types of fields.  In order to do that, you must implement a *GsonBuilderCustomizer*:

```java
public interfac GsonBuilderCustomizer {
  void customize(GsonBuilder builder);
}
```

When a GsonBuilderCustomizer is detected by the MicrobuleContainer, it will be used to customize the *Gson* object used by Microbule.  Feel free to check out the GSON [User Guide](https://github.com/google/gson/blob/master/UserGuide.md) for more information on how you can customize GSON.
