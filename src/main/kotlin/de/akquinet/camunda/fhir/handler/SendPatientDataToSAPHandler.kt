package de.akquinet.camunda.fhir.handler

import de.akquinet.camunda.fhir.service.PatientData
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SendPatientDataToSAPHandler {
    private val log: Logger = LoggerFactory.getLogger(SendPatientDataToSAPHandler::class.java)

    @JobWorker(type = "send-patientdata_to_sap")
    fun setPatientAddressToSAP(@VariablesAsType variables: PatientData) {
        log.info("sending patient data to SAP: {}", variables)
    }
}