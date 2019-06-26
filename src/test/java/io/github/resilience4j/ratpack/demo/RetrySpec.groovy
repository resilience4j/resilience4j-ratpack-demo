package io.github.resilience4j.ratpack.demo

import io.netty.handler.codec.http.HttpResponseStatus
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class RetrySpec extends Specification {

    static String BACKEND_A = "backendA"

    @Shared
    EmbeddedApp app

    @Shared
    TestHttpClient client

    def "should retry 3 times"() {
        given:
        app = GroovyEmbeddedApp.fromServer(Application.app())
        client = app.httpClient
        app.server.start()

        when:
        produceFailure(BACKEND_A)

        then:
        checkMetrics("failed_with_retry", BACKEND_A, "1.0")

        cleanup:
        client.resetRequest()
        app.server.stop()

    }

    def "should succeed without retry"() {
        given:
        app = GroovyEmbeddedApp.fromServer(Application.app())
        client = app.httpClient
        app.server.start()

        when:
        produceSuccess(BACKEND_A)

        then:
        checkMetrics("successful_without_retry", BACKEND_A, "1.0")

        cleanup:
        client.resetRequest()
        app.server.stop()

    }

    def checkMetrics(String kind, String backend, String count) {
        def response = client.get("/actuator/prometheus").body.text
		response.contains("resilience4j_retry_calls{name=\"" +  backend + "\",kind=\"" + kind + "\",} " + count)
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
