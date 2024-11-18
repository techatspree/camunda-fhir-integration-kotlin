# Demo for integrating a Fhir ressource into Camunda Workflows

## The demo process
We are showcasing two approaches to integrate a Fhir ressource into a camunda workflow.

The demo process definition is based of a typical integration task to fetch some information from system A to put it into a system B.

![camunda_under_fhir_hapi.png](src/main/resources/camunda_under_fhir_hapi.png)


Our process definition consists of two service tasks. The first one is responsible to fetch some patient data from 
a Fhir server and populate the data as process variables.
The second service task simulates to write the patient data into another 3rd party system like SAP 
(for demo purposes it does log out the patient data only).

Fetching the data from a Fhir server can be done in several ways with camunda. We are demonstrating two soliutions:

* Implementing a job handler by using the popular HAPI Fhir client (https://hapifhir.io) 
* A no code solution with the provided camunda REST connector


## How to run the examples

To run the demo successfully the following requirements have to be met:
* Java 21 installed
* A running Docker daemon 
* Internet connection to have access to the HAPI Test Fhir server
* Installed and running Camunda 8 Run instance (for the REST connector example)

### Camunda 8 Run
Camunda 8 Run is a simple and lightweight Camunda 8 distribution for a local developer environment.
To download and install Camunda 8 run please have a look into this documentation: https://docs.camunda.io/docs/self-managed/setup/deploy/local/c8run/#install-and-start-camunda-8-run

## Build and Run the testcases

    $./mvnw clean install

This will build the project and run all the test cases. These test cases will deploy, start the process instances
and will do some assertions.

## Hapi Fhir solution
The following process definition does include the Hapi Fhir client in a custom jobhandler implementation.

Process definition:
* [camunda_under_fhir_hapi.bpmn](src/main/resources/camunda_under_fhir_hapi.bpmn)

The test cases showcasing this particular approach:
* [ProcessHapiTest.kt](src/test/kotlin/de/akquinet/camunda/fhir/ProcessHapiTest.kt)
* [ProcessHapiIntegrationTest.kt](src/test/kotlin/de/akquinet/camunda/fhir/ProcessHapiIntegrationTest.kt)

The first test case does test the process in mocked environment. While the second test case is performing
a full integration test.

## Camunda 8 REST connector solution
The following process definition does include the usage of the Camunda 8 REST connector involving configuration only.

Process definition:
* [camunda_under_fhir_connector.bpmn](src/main/resources/camunda_under_fhir_connector.bpmn)

The test cases showcasing this particular approach:
* [ProcessConnectorTest.kt](src/test/kotlin/de/akquinet/camunda/fhir/ProcessConnectorTest.kt)
* [ProcessConnectorIntegrationTest.kt](src/test/kotlin/de/akquinet/camunda/fhir/ProcessConnectorIntegrationTest.kt)

The first test case does test the process in mocked environment. While the second test case is performing
a full integration test.

The integration test for the Rest connector is not enabled by default. It does need a running Camudna 8 Run instance.
So after starting Camunda 8 run just start the integration test from your favorite IDE or by using the maven cli:

    $./mvnw test -Dtest=de.akquinet.camunda.fhir.ProcessConnectorIntegrationTest


