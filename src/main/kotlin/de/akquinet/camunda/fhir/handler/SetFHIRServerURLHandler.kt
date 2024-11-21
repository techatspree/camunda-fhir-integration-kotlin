package de.akquinet.camunda.fhir.handler

import io.camunda.zeebe.spring.client.annotation.JobWorker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class  SetFHIRServerURLHandler(@Value("\${hapi.fhir.serverbase}") val serverURL: String) {
    private val log: Logger = LoggerFactory.getLogger(SetFHIRServerURLHandler::class.java)

    @JobWorker(type = "set-fhir-server-url")
    fun setFHIRServerURL(): Map<String, String>  {
        log.info("setting FHIR server URL: {}", serverURL)

        return hashMapOf("fhirServerUrl" to serverURL)
    }
}