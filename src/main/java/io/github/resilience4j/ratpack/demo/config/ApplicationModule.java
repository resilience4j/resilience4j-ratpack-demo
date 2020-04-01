package io.github.resilience4j.ratpack.demo.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratpack.demo.chain.BackendAChain;
import io.github.resilience4j.ratpack.demo.chain.BackendBChain;
import io.github.resilience4j.ratpack.demo.chain.BackendCChain;
import io.github.resilience4j.ratpack.demo.connector.BackendAConnector;
import io.github.resilience4j.ratpack.demo.connector.BackendBConnector;
import io.github.resilience4j.ratpack.demo.connector.BackendCConnector;
import io.github.resilience4j.ratpack.demo.connector.Connector;
import io.github.resilience4j.ratpack.demo.service.BusinessAService;
import io.github.resilience4j.ratpack.demo.service.BusinessBService;
import io.github.resilience4j.ratpack.demo.service.BusinessCService;
import io.github.resilience4j.ratpack.demo.service.BusinessService;
import ratpack.exec.ExecController;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import java.util.concurrent.TimeUnit;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Connector.class).annotatedWith(Names.named("backendAConnector")).to(BackendAConnector.class).in(Scopes.SINGLETON);
        bind(Connector.class).annotatedWith(Names.named("backendBConnector")).to(BackendBConnector.class).in(Scopes.SINGLETON);
        bind(Connector.class).annotatedWith(Names.named("backendCConnector")).to(BackendCConnector.class).in(Scopes.SINGLETON);
        bind(BusinessService.class).annotatedWith(Names.named("businessAService")).to(BusinessAService.class).in(Scopes.SINGLETON);
        bind(BusinessService.class).annotatedWith(Names.named("businessBService")).to(BusinessBService.class).in(Scopes.SINGLETON);
        bind(BusinessService.class).annotatedWith(Names.named("businessCService")).to(BusinessCService.class).in(Scopes.SINGLETON);
        bind(BackendAChain.class).in(Scopes.SINGLETON);
        bind(BackendBChain.class).in(Scopes.SINGLETON);
        bind(BackendCChain.class).in(Scopes.SINGLETON);
        bind(StartupService.class).in(Scopes.SINGLETON);
    }

    static class StartupService implements Service {
        @Override
        public void onStart(StartEvent event) throws Exception {
            CircuitBreakerRegistry registry = event.getRegistry().get(CircuitBreakerRegistry.class);
            Runnable cmd = () ->
                    registry.getAllCircuitBreakers().forEach(cb -> {
                        cb.onSuccess(1000, TimeUnit.NANOSECONDS);
                        cb.onError(1000, TimeUnit.NANOSECONDS, new Exception("exception"));
                    });
            ExecController.current().ifPresent(e ->
                    e.getExecutor().scheduleAtFixedRate(cmd, 5, 5, TimeUnit.SECONDS)
            );
        }
    }
}
