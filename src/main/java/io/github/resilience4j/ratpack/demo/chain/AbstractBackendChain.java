package io.github.resilience4j.ratpack.demo.chain;

import io.github.resilience4j.ratpack.demo.service.BusinessService;
import ratpack.func.Action;
import ratpack.handling.Chain;

public abstract class AbstractBackendChain implements Action<Chain> {

    protected Chain backendChain(Chain chain, BusinessService service) {
        return chain
                .get("failure", ctx -> ctx.render(service.failure()))
                .get("success", ctx -> ctx.render(service.success()))
                .get("ignore", ctx -> ctx.render(service.ignore()))
                .get("monoSuccess", ctx -> service.monoSuccess().subscribe(ctx::render))
                .get("monoFailure", ctx -> service.monoFailure().subscribe(ctx::render))
                .get("fluxSuccess", ctx -> service.fluxSuccess().subscribe(ctx::render))
                .get("fluxFailure", ctx -> service.fluxFailure().subscribe(ctx::render))
                .get("futureSuccess", ctx -> ctx.render(service.futureSuccess().get()))
                .get("futureFailure", ctx -> ctx.render(service.futureFailure().get()))
                .get("promiseSuccess", ctx -> service.promiseSuccess().then(ctx::render))
                .get("promiseFailure", ctx -> service.promiseFailure().then(ctx::render))
                .get("fallback", ctx -> ctx.render(service.failureWithFallback()));
    }
}
