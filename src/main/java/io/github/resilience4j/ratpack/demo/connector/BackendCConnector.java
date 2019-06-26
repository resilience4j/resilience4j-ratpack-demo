package io.github.resilience4j.ratpack.demo.connector;


import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratpack.demo.exception.BusinessException;
import io.github.resilience4j.ratpack.demo.exception.ServerException;
import io.github.resilience4j.retry.annotation.Retry;
import io.netty.handler.codec.http.HttpResponseStatus;
import ratpack.exec.Promise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Named;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;

@CircuitBreaker(name = "backendC")
@Retry(name = "backendC")
@Named("backendCConnector")
public class BackendCConnector implements Connector {

    @Override
    @Bulkhead(name = "backendC")
    public String failure() {
        throw new ServerException("This is a remote exception", HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @Bulkhead(name = "backendC")
    public String success() {
        return "Hello World from backend C";
    }

    @Override
    public String ignoreException() {
        throw new BusinessException("This exception is ignored by the CircuitBreaker of backend C");
    }

    @Override
    @Bulkhead(name = "backendC")
    public Flux<String> fluxFailure() {
        return Flux.error(new IOException("BAM!"));
    }

    @Override
    @Bulkhead(name = "backendC")
    public Mono<String> monoSuccess() {
        return Mono.just("Hello World from backend C");
    }

    @Override
    @Bulkhead(name = "backendC")
    public Mono<String> monoFailure() {
        return Mono.error(new IOException("BAM!"));
    }

    @Override
    @Bulkhead(name = "backendC")
    public Flux<String> fluxSuccess() {
        return Flux.just("Hello", "World");
    }

    @Override
    @Bulkhead(name = "backendC", type = Type.THREADPOOL)
    public CompletableFuture<String> futureSuccess() {
        return CompletableFuture.completedFuture("Hello World from backend C");
    }

    @Override
    @Bulkhead(name = "backendC", type = Type.THREADPOOL)
    public CompletableFuture<String> futureFailure() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new IOException("BAM!"));
        return future;
    }

    @Override
    @Bulkhead(name = "backendC", type = Bulkhead.Type.THREADPOOL)
    public Promise<String> promiseSuccess() {
        return Promise.value("Hello World from backend C");
    }

    @Override
    @Bulkhead(name = "backendC", type = Bulkhead.Type.THREADPOOL)
    public Promise<String> promiseFailure() {
        return Promise.error(new IOException("BAM!"));
    }

    @Override
    @CircuitBreaker(name = "backendC", fallbackMethod = "fallback")
    public String failureWithFallback() {
        return failure();
    }

    private String fallback(Throwable ex) {
        return "Recovered " + ex.getMessage();
    }
}
