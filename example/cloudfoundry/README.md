# Microbule Cloud Foundry Example

## Building

```text
mvn clean package
```

## Running Locally

```text
export CF_INSTANCE_ADDR=localhost:8080
mvn spring-boot:run
```

## Running in Cloud Foundry

- Login to Cloud Foundry

```
cf login -a {API-ENDPOINT}
```

- Push Application

```
cf push {APP-NAME} -p target/microbule-example-cloudfoundry-${version}.jar
```