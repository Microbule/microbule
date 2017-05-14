# Contexts and Dependency Injection (CDI)

Microbule is designed to be automatically detected by your CDI container.  All of Microbule's components are assigned JSR-330 (_javax.inject_) annotations.  First, you'll need to add Microbule to your classpath.

# Dependencies

If you are using Maven, simply add the following dependencies to your pom.xml file:

```xml
<dependency>
  <groupId>org.microbule</groupId>
  <artifactId>microbule-all</artifactId>
  <version>${microbule.version}</version>
<dependency>

<dependency>
  <groupId>org.microbule</groupId>
  <artifactId>microbule-cdi-container</artifactId>
  <version>${microbule.version}</version>
<dependency>
```

Let's examine these dependencies:

- **microbule-all**: the Microbule framework with all of the standard plugins.
- **microbule-cdi-container**: the CDI-based *MicrobuleContainer* implementation, which adapts Microbule to CDI.

You will also need to choose from the available [ConfigurationProviders](configuration.md).  

Once you have these dependencies defined, Microbule will automatically start up once the CDI "application" scope initializes.

# JAX-RS Service Discovery

Once Microbule starts, it will automatically create a CXF server for any `@Path`-annotated bean in the current CDI container.
