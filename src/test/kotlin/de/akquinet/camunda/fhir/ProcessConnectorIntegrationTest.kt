package de.akquinet.camunda.fhir

import io.camunda.zeebe.client.ZeebeClient
import org.awaitility.Awaitility
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import java.time.Duration

@SpringBootTest
@Disabled("A running C8RUN instance expected")
class ProcessConnectorIntegrationTest(@Autowired val client: ZeebeClient) {

    @Test
    fun testDeploymentAndStartProcessInstance() {
        val processInstance = client
            .newCreateInstanceCommand()
            .bpmnProcessId("camundafhirconnector")
            .latestVersion()
            .variable("patientId", "example")
            .send()
            .join()


        val restClient = RestClient.create()
        restClient
            .post()
            .uri("http://localhost:8080/api/login?username=demo&password=demo")
            .retrieve()

        Awaitility
            .await()
            .atMost(Duration.ofSeconds(30))
            .until { checkForProcessInstanceIsCompleted(restClient, processInstance.processInstanceKey) }
    }

    private fun checkForProcessInstanceIsCompleted(restClient: RestClient, processInstanceKey: Long): Boolean {
        val result = restClient
            .post()
            .uri("http://localhost:8080/v1/process-instances/search")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """{
                       "filter": { 
                           "key": $processInstanceKey    
                        }
                }"""
            )
            .retrieve()
            .toEntity(String::class.java)
        return result.statusCode.is2xxSuccessful && result.body!!.contains("COMPLETED")
    }
}
