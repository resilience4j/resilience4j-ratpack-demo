package io.github.resilience4j.ratpack.demo.service;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratpack.circuitbreaker.CircuitBreakerTransformer;
import io.github.resilience4j.ratpack.demo.connector.Connector;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import ratpack.exec.Promise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletableFuture;

@Named("businessBService")
public class BusinessBService implements BusinessService {

    private final Connector backendConnector;
    private final CircuitBreaker circuitBreaker;

    @Inject
    public BusinessBService(@Named("backendBConnector") Connector backendConnector,
                            CircuitBreakerRegistry circuitBreakerRegistry) {
        this.backendConnector = backendConnector;
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendB");
    }

    public String failure() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, backendConnector::failure).get();
    }

    public String success() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, backendConnector::success).get();
    }

    @Override
    public String ignore() {
        return CircuitBreaker.decorateSupplier(circuitBreaker, backendConnector::ignoreException).get();
    }

    @Override
    public Flux<String> fluxFailure() {
        return backendConnector.fluxFailure()
                .transform(CircuitBreakerOperator.of(circuitBreaker));
    }

    @Override
    public Mono<String> monoSuccess() {
        return backendConnector.monoSuccess()
                .transform(CircuitBreakerOperator.of(circuitBreaker));
    }

    @Override
    public Mono<String> monoFailure() {
        return backendConnector.monoFailure()
                .transform(CircuitBreakerOperator.of(circuitBreaker));
    }

    @Override
    public Flux<String> fluxSuccess() {
        return backendConnector.fluxSuccess()
                .transform(CircuitBreakerOperator.of(circuitBreaker));
    }

    @Override
    public CompletableFuture<String> futureSuccess() {
        return backendConnector.futureSuccess();
    }

    @Override
    public CompletableFuture<String> futureFailure() {
        return backendConnector.futureFailure();
    }

    @Override
    public Promise<String> promiseSuccess() {
        return backendConnector.promiseSuccess()
                .transform(CircuitBreakerTransformer.of(circuitBreaker));
    };

    @Override
    public Promise<String> promiseFailure() {
        return backendConnector.promiseFailure()
                .transform(CircuitBreakerTransformer.of(circuitBreaker));
    };

    @Override
    public String failureWithFallback() {
        return backendConnector.failureWithFallback();
    }
}
