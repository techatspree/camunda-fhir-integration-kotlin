package de.akquinet.camunda.fhir

import io.camunda.zeebe.client.ZeebeClient
import io.camunda.zeebe.client.api.response.DeploymentEvent
import io.camunda.zeebe.process.test.api.ZeebeTestEngine
import io.camunda.zeebe.process.test.assertions.BpmnAssert
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.TimeoutException

@ZeebeProcessTest
class ProcessHapiTest {
    private lateinit var engine: ZeebeTestEngine
    private lateinit var client: ZeebeClient

    @Test
    fun testDeploymentAndStartProcessInstance() {
        val deploymentEvent = deployProcessDef("camunda_under_fhir_hapi.bpmn")
        val assertions = BpmnAssert.assertThat(deploymentEvent)
        assertions.containsProcessesByBpmnProcessId("camundafhirhapi")
    }

    @Test
    @Throws(InterruptedException::class, TimeoutException::class)
    fun testStartProcessInstance() {
        deployProcessDef("camunda_under_fhir_hapi.bpmn")
        val event = client.newCreateInstanceCommand()
            .bpmnProcessId("camundafhirhapi")
            .latestVersion()
            .send()
            .join()

        completeServiceTasks("get-patient-address", 1)
        completeServiceTasks("send-patientdata_to_sap", 1)

        val assertions = BpmnAssert.assertThat(event)
        assertions.hasPassedElementsInOrder("StartEvent", "getPatientData", "setPatientData", "EndEvent")
        assertions.isCompleted()
    }

    private fun deployProcessDef(processDefinition: String): DeploymentEvent {
        return client.newDeployResourceCommand()
            .addResourceFromClasspath(processDefinition)
            .send()
            .join()
    }

    private fun completeServiceTasks(jobType: String, count: Int) {
        val activateJobsResponse =
            client.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(count).send().join()

        val activatedJobCount = activateJobsResponse.jobs.size
        if (activatedJobCount < count) {
            Assertions.fail<Any>(
                "Unable to activate %d jobs, because only %d were activated."
                    .format(count, activatedJobCount)
            )
        }

        for (i in 0..<count) {
            val job = activateJobsResponse.jobs[i]

            client.newCompleteCommand(job.key).send().join()
        }

        engine.waitForIdleState(Duration.ofSeconds(1))
    }
}
