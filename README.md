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
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | YES  | Tested with **Java** and **PHP** apps |
| Start / Stop / Remove |   YES     |   - |
| Scaling capabilities     | YES       |  Can scale number of instances, disk and RAM |
| Services management |   YES     |   Tested with **ClearDB** from Pivotal |
| Environment variables management |   YES     |   Read & Write |

| Limitations  |
| ------------- ||
| - |
---
+ [Heroku](https://github.com/heroku/heroku.jar)
<dl>
  <dt>Description</dt>
  <dd></dd>
  <dt>Features</dt>
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | YES  | Tested with **Java** |
| Start / Stop / Remove |   YES     |   - |
| Scaling capabilities     | YES       |  Can scale number of instances |
| Services management |   YES     |   Tested with **ClearDB** from Pivotal |
| Environment variables management |   YES     |   Read & Write |

| Limitations  |
| ------------- ||
| - |
---

+ [Openshift v2](https://github.com/openshift/openshift-java-client) (used by Openshift Online)
<dl>
  <dt>Description</dt>
  <dd></dd>
  <dt>Features</dt>
</dl>

| Feature       | Supported?    | Comments  |
| ------------- |:-------------:|:-----|
| Application deployment      | YES  | Only from GIT |
| Start / Stop / Remove |   YES     |   - |
| Scaling capabilities     | YES       |  Can scale number of instances |
| Services management |   YES     |   Tested with **MySQL** from [Openshift Online](https://openshift.redhat.com) |
| Environment variables management |   -     |  -  |

| Limitations  |
| ------------- ||
| - |
---

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
