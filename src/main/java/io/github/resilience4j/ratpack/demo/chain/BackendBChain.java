package io.github.resilience4j.ratpack.demo.chain;

import io.github.resilience4j.ratpack.demo.service.BusinessService;
import ratpack.handling.Chain;

import javax.inject.Inject;
import javax.inject.Named;

public class BackendBChain extends AbstractBackendChain {

    private BusinessService service;

    @Inject
    public BackendBChain(@Named("businessBService") BusinessService service) {
        this.service = service;
    }

    @Override
    public void execute(Chain chain) throws Exception {
        backendChain(chain, service);
    }
}
