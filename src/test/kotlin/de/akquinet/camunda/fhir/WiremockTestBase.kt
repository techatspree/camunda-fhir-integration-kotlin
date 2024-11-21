package de.akquinet.camunda.fhir

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.marcinziolo.kotlin.wiremock.equalTo
import com.marcinziolo.kotlin.wiremock.get
import com.marcinziolo.kotlin.wiremock.returnsJson
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class WiremockTestBase {

    private val port = 8081
    val wiremock: WireMockServer = WireMockServer(options().port(port).notifier(ConsoleNotifier(true)))

    @BeforeEach
    fun setUp() {
        wiremock.start()
        wiremock.get {
            url equalTo  "/Patient/example"
        } returnsJson {
            body = ProcessHapiIntegrationTest::class.java.getResource("/hapi-fhir-response.json").readText()
        }
        wiremock.get {
            url equalTo  "/metadata"
        } returnsJson {
            body = ProcessHapiIntegrationTest::class.java.getResource("/metadata-response.json").readText()
        }
    }

    @AfterEach
    fun afterEach() {
        wiremock.resetAll()
        wiremock.stop()
    }
}