package io.github.resilience4j.ratpack.demo

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.ratpack.circuitbreaker.monitoring.endpoint.states.CircuitBreakerStatesEndpointResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.vavr.collection.Stream
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CircuitBreakerSpec extends Specification {

    static String BACKEND_A = "backendA"
    static String BACKEND_B = "backendB"

    @Shared
    EmbeddedApp app

    @Shared
    TestHttpClient client

    def "should open and close #name circuitbreaker"() {
        given:
        app = GroovyEmbeddedApp.fromServer(Application.app())
        client = app.httpClient
        app.server.start()

        when:
        Stream.rangeClosed(1, count).forEach { count -> produceFailure(name) }

        then:
        checkStatus(name, CircuitBreaker.State.OPEN)
        Thread.sleep(2000)

        when:
        Stream.rangeClosed(1, 3).forEach { count -> produceSuccess(name) }

        then:
        checkStatus(name, CircuitBreaker.State.CLOSED)

        cleanup:
        client.resetRequest()
        app.server.stop()

        where:
        name      | count
        BACKEND_A | 5
        BACKEND_B | 10
    }

    def checkStatus(String circuitBreakerName, CircuitBreaker.State state) {
        def mapper = app.server.registry.get().get(ObjectMapper)
        def data = client.get("/circuitbreaker/states/$circuitBreakerName").body.text
        def response = mapper.readValue(data, CircuitBreakerStatesEndpointResponse)
        response.circuitBreakerStates[0].currentState == state
    }

    def produceFailure(String backend) {
        def response = client.get("/" + backend + "/failure")
        assert response.statusCode == HttpResponseStatus.INTERNAL_SERVER_ERROR.code()
    }

    def produceSuccess(String backend) {
        def response = client.get("/" + backend + "/success")
        assert response.statusCode == HttpResponseStatus.OK.code()
    }
}
