package de.akquinet.camunda.fhir.service

import ca.uhn.fhir.context.FhirContext
import org.hl7.fhir.r4.model.Patient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PatientFHIRService(val fhirContext: FhirContext,
                         @Value("\${hapi.fhir.serverbase}") val serverBase: String) {

    private val log: Logger = LoggerFactory.getLogger(PatientFHIRService::class.java)


    fun getPatientData(patientData: PatientData): PatientData {
        // Create a client
        val client = fhirContext.newRestfulGenericClient(serverBase)

        // Read a patient with the given ID
        val patient = client.read().resource(Patient::class.java).withId(patientData.patientId).execute()

        // Print the output
        val string = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient)
        log.info("Patient from FHIR: {}", string)

        val firstAddress = patient.addressFirstRep
        patientData.city = firstAddress.city
        patientData.country = firstAddress.country
        patientData.state = firstAddress.state
        patientData.zip = firstAddress.postalCode
        patientData.street = firstAddress.line.joinToString { stringType -> stringType.value }
        patientData.use = firstAddress.use.name

        return patientData
    }
}