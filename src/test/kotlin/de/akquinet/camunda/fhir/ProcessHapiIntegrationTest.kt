package de.akquinet.camunda.fhir

import io.camunda.process.test.api.CamundaAssert
import io.camunda.process.test.api.CamundaSpringProcessTest
import io.camunda.zeebe.client.ZeebeClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["hapi.fhir.serverbase=http://localhost:8081/"])
@CamundaSpringProcessTest
class ProcessHapiIntegrationTest(@Autowired val client: ZeebeClient): WiremockTestBase() {

    @Test
    fun testDeploymentAndStartProcessInstance() {
        val processInstance = client
            .newCreateInstanceCommand()
            .bpmnProcessId("camundafhirhapi")
            .latestVersion()
            .variable("patientId", "example")
            .send()
            .join()

        CamundaAssert.assertThat(processInstance)
            .hasVariable("patientId", "example")
            .hasVariable("city", "PleasantVille")
            .hasVariable("state", "Vic")
            .hasVariable("zip", "3999")
            .hasVariable("street", "534 Erewhon St")
            .hasVariable("use", "HOME")
            .isCompleted()
    }
}
