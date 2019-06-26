package io.github.resilience4j.ratpack.demo.service;

import io.github.resilience4j.ratpack.demo.connector.Connector;
import ratpack.exec.Promise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletableFuture;

@Named("businessCService")
public class BusinessCService implements BusinessService {

    private final Connector backendConnector;

    @Inject
    public BusinessCService(@Named("backendCConnector") Connector backendConnector){
        this.backendConnector = backendConnector;
    }

    @Override
    public String failure() {
        return backendConnector.failure();
    }

    @Override
    public String success() {
        return backendConnector.success();
    }

    @Override
    public String ignore() {
        return backendConnector.ignoreException();
    }


    @Override
    public Flux<String> fluxFailure() {
        return backendConnector.fluxFailure();
    }

    @Override
    public Mono<String> monoSuccess() {
        return backendConnector.monoSuccess();
    }

    @Override
    public Mono<String> monoFailure() {
        return backendConnector.monoFailure();
    }

    @Override
    public Flux<String> fluxSuccess() {
        return backendConnector.fluxSuccess();
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
        return backendConnector.promiseSuccess();
    };

    @Override
    public Promise<String> promiseFailure() {
        return backendConnector.promiseFailure();
    };

    @Override
    public String failureWithFallback() {
        return backendConnector.failureWithFallback();
    }
}
