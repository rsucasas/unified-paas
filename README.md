# unified-paas
SeaClouds Unified PaaS Library

## Usage ##

1. Compile needed projects

* https://github.com/rosogon/heroku-maven-plugin. Compile with `mvn install –P complete`. It generates the artifact with all the dependencies and copy them in mvn repository.

* https://github.com/rosogon/heroku-java-client. Compile with `mvn install –Dmaven.test.skip`. 

* https://github.com/rosogon/unified-paas. To use the test, API_KEY with heroku key must be set.

2. Start server: `java -jar service/target/unified-paas-service-0.0.1-SNAPSHOT.jar`

3. Deploy an application: 

`curl http://localhost:8080/heroku/applications -X POST -F file=@"<warfile>" -F model='{"name":"samplewar"}' -H"Content-Type: multipart/form-data" -H"apikey:<heroku-api-key>"`

### Unit and integration tests / Install ###

##### Configuration #####
* Set values in /library/src/test/resources/tests.config.properties

##### Unit tests #####
* Execute tests: `mvn clean test -P dev`
* Install: `mvn clean install -P dev`

##### Integration tests #####
* Execute tests: `mvn clean verify -P integration-test`
* Install: `mvn clean install -P integration-test`

##### Skip tests #####
* Install: `mvn clean install`

