package de.akquinet.camunda.fhir

import io.camunda.zeebe.spring.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Deployment(resources = ["classpath:camunda_under_fhir_hapi.bpmn", "classpath:camunda_under_fhir_connector.bpmn"])
class CamundaFhirIntegrationApplication

fun main(args: Array<String>) {
	runApplication<CamundaFhirIntegrationApplication>(*args)
}

annotation class NoArg()