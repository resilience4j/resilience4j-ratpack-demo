package io.github.resilience4j.ratpack.demo;

import io.github.resilience4j.ratpack.Resilience4jConfig;
import io.github.resilience4j.ratpack.Resilience4jModule;
import io.github.resilience4j.ratpack.demo.chain.BackendAChain;
import io.github.resilience4j.ratpack.demo.chain.BackendBChain;
import io.github.resilience4j.ratpack.demo.chain.BackendCChain;
import io.github.resilience4j.ratpack.demo.config.ApplicationModule;
import ratpack.dropwizard.metrics.DropwizardMetricsModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

public class Application {

    public static RatpackServer app() throws Exception {
        return RatpackServer.of(s -> s
                .serverConfig(c -> c
                        .development(true)
                        .yaml(Application.class.getClassLoader().getResource("application.yml"))
                        .require("/resilience4j", Resilience4jConfig.class)
                )
                .registry(Guice.registry(b -> b
                        .module(ApplicationModule.class)
                        .module(Resilience4jModule.class)
                        .module(DropwizardMetricsModule.class)
                ))
                .handlers(c -> c
                        .prefix("backendA", BackendAChain.class)
                        .prefix("backendB", BackendBChain.class)
                        .prefix("backendC", BackendCChain.class)
                )
        );
    }
    public static void main(String[] args) throws Exception {
        app().start();
    }

}
