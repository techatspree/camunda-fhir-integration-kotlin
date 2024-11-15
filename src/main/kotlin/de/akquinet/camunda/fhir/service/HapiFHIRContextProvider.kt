package de.akquinet.camunda.fhir.service

import ca.uhn.fhir.context.FhirContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class HapiFHIRContextProvider {

    @Bean
    fun createFhirContext(): FhirContext = FhirContext.forR4()
}