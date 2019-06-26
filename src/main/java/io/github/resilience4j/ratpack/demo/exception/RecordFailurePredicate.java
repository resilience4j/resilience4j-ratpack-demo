package io.github.resilience4j.ratpack.demo.exception;

import java.util.function.Predicate;

public class RecordFailurePredicate implements Predicate<Throwable> {

    @Override
    public boolean test(Throwable throwable) {
        return !(throwable instanceof BusinessException);
    }

}