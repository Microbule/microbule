# Pushing to Cloud Foundry

- Run the build

```
mvn clean package
```

- Login to Cloud Foundry

```
cf login -a {API-ENDPOINT}
```

- Push Application

```
cf push {APP-NAME} -p target/microbule-example-cloudfoundry-${version}.jar
```