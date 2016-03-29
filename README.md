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

+ [Cloud Foundry](https://github.com/cloudfoundry/cf-java-client)
<dl>
  <dt>Description</dt>
  <dd></dd>
  <dt>Features</dt>
  <dd></dd>
  <dt>Usage (maven - pom.xml)</dt>
  <dd>
```
  <dependency>
    <groupId>org.cloudfoundry</groupId>
    <artifactId>cloudfoundry-client-lib</artifactId>
    <version>${cf-version}</version>
	</dependency>
```
  </dd>
</dl>
+ [Heroku](https://github.com/heroku/heroku.jar)
<dl>
  <dt>Description</dt>
  <dd>Two libraries have been used for Heroku...</dd>
  <dt>Features</dt>
  <dd></dd>
  <dt>Usage (maven - pom.xml)</dt>
  <dd>
```
  <dependency>
      <groupId>com.heroku.api</groupId>
      <artifactId>heroku-api</artifactId>
      <version>${heroku.version}</version>
  </dependency>
  <dependency>
      <groupId>com.heroku.api</groupId>
      <artifactId>heroku-json-jackson</artifactId>
      <version>${heroku.version}</version>
  </dependency>
  <dependency>
  	<groupId>com.heroku.sdk</groupId>
  	<artifactId>heroku-deploy</artifactId>
  	<version>${heroku-deploy}</version>
  </dependency>
  <dependency>
      <groupId>com.heroku.api</groupId>
      <artifactId>heroku-http-apache</artifactId>
      <version>${heroku.version}</version>
  </dependency>
```
  </dd>
</dl>
+ [Openshift v2](https://github.com/openshift/openshift-java-client) (used by Openshift Online)
<dl>
  <dt>Description</dt>
  <dd></dd>
  <dt>Features</dt>
  <dd></dd>
  <dt>Usage (maven - pom.xml)</dt>
  <dd>
```
  <dependency>
		<groupId>com.openshift</groupId>
		<artifactId>openshift-java-client</artifactId>
		<version>${openshift2-version}</version>
	</dependency>
```
  </dd>
</dl>
+ [Openshift v3](https://github.com/openshift/openshift-restclient-java)  (new version for the version 3 architecture of OpenShift based on Kubernetes: OpenShift Enterprise, local Openshift installations ...)
<dl>
  <dt>Description</dt>
  <dd>-Not implemented-</dd>
  <dt>Features</dt>
  <dd>-</dd>
</dl>

---

| Client        | Version           | URL  |
| ------------- |:-------------:| :-----|
| Cloud Foundry      | 1.1.3  | https://github.com/cloudfoundry/cf-java-client |
| Heroku      | 0.16 / 0.5.7 *       |   https://github.com/heroku/heroku.jar |
| Openshift v2 |   2.7.0.Final     |    https://github.com/openshift/openshift-java-client |
| Openshift v3 |   -     |    https://github.com/openshift/openshift-restclient-java |
