# unified-paas
SeaClouds Unified PaaS Library

## Usage
1. Compile needed projects

* https://github.com/rosogon/heroku-java-client. Compile with `mvn install –Dmaven.test.skip`. 

* https://github.com/rosogon/unified-paas. To use the test, API_KEY with heroku key must be set.

2. Start server: `java -jar service/target/unified-paas-service-0.0.1-SNAPSHOT.jar`

3. Deploy an application: 

`curl http://localhost:8080/heroku/applications -X POST -F file=@"<warfile>" -F model='{"name":"samplewar"}' -H"Content-Type: multipart/form-data" -H"apikey:<heroku-api-key>"`

### Integration tests

##### Configuration
* Set values in /library/src/test/resources/tests.config.properties

##### Integration Tests
* Execute tests: `mvn clean verify -P integration-test`

## Java Client Libraries
Java client libraries used in the project:

1. [Cloud Foundry](https://github.com/cloudfoundry/cf-java-client)
```
https://github.com/cloudfoundry/cf-java-client
```
2. [Heroku](https://github.com/heroku/heroku.jar)
```
https://github.com/heroku/heroku.jar
```
3. [Openshift v2](https://github.com/openshift/openshift-java-client) (used by Openshift Online)
```
https://github.com/openshift/openshift-java-client 
```
4. [Openshift v3](https://github.com/openshift/openshift-restclient-java)  (new version for the version 3 architecture of OpenShift based on Kubernetes: OpenShift Enterprise, local Openshift installations ...)
```
https://github.com/openshift/openshift-restclient-java
```
