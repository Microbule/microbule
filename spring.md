# Spring Framework

Microbule is designed to be automatically loaded into your Spring ApplicationContext.  All of Microbule's components are assigned JSR-330 (_javax.inject_) annotations.  First, you'll need to put Microbule on the classpath.

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
  <artifactId>microbule-spring-container</artifactId>
  <version>${microbule.version}</version>
<dependency>

<dependency>
  <groupId>org.microbule</groupId>
  <artifactId>microbule-spring-config</artifactId>
  <version>${microbule.version}</version>
<dependency>
```

Let's examine these dependencies:

- **microbule-all**: the Microbule framework with all of the standard plugins.
- **microbule-spring-container**: the Spring-based MicrobuleContainer implementation, which adapts Microbule to Spring.
- **microbule-spring-config**: the Spring-based ConfigurationProvider, which allows Microbule to be configured using Spring's PropertySource abstraction.

Once you have these dependencies defined, you can easily load Microbule into your ApplicationContext.

# Loading Microbule

Since Microbule uses JSR-330, it can be automatically detected using Spring's component scan feature.  For example, if you are using Spring's Java-based configuration:

```java
@Configuration
@ComponentScan(basePackages = {"org.microbule","com.myco.myproj"})
@PropertySource("classpath:application.properties")
public class MyApplicationConfiguration {

}
```

# JAX-RS Service Discovery

Once Microbule starts, it will automatically create a CXF server for any @Path-annotated bean in the current ApplicationContext.
