package de.akquinet.camunda.fhir.handler

import de.akquinet.camunda.fhir.service.PatientData
import de.akquinet.camunda.fhir.service.PatientFHIRService
import io.camunda.zeebe.spring.client.annotation.JobWorker
import io.camunda.zeebe.spring.client.annotation.VariablesAsType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GetPatientAddressHandler(val patientService: PatientFHIRService) {

    private val log: Logger = LoggerFactory.getLogger(GetPatientAddressHandler::class.java)

    @JobWorker(type = "get-patient-address")
    fun getPatientAddress(@VariablesAsType patientData: PatientData): PatientData {
        log.info("patientId: {}", patientData.patientId)

        return patientService.getPatientData(patientData)
    }

}