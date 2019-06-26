package io.github.resilience4j.ratpack.demo.connector;

import ratpack.exec.Promise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface Connector {

    String failure();

    String success();

    String ignoreException();

    Mono<String> monoSuccess();

    Mono<String> monoFailure();

    Flux<String> fluxSuccess();

    Flux<String> fluxFailure();

    CompletableFuture<String> futureSuccess();

    CompletableFuture<String> futureFailure();

    Promise<String> promiseSuccess();

    Promise<String> promiseFailure();

    String failureWithFallback();

}
