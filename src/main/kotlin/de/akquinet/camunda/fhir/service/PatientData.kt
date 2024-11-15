package de.akquinet.camunda.fhir.service

import de.akquinet.camunda.fhir.NoArg

@NoArg
data class PatientData(
            var patientId: String,
            var name: String?,
            var use: String?,
            var street: String?,
            var city: String?,
            var state: String?,
            var zip: String?,
            var country: String?
)