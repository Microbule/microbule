# GSON

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
