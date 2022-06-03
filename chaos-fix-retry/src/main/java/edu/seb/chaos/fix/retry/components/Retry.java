package edu.seb.chaos.fix.retry.components;

import edu.seb.chaos.fix.retry.components.impl.RetryImpl;
import edu.seb.chaos.fix.retry.config.RetryConfiguration;

import java.util.concurrent.Callable;
import java.util.function.Function;

public interface Retry {

    @SuppressWarnings("rawtypes")
    static Retry of(RetryConfiguration configuration) {
        return new RetryImpl(configuration);
    }

    <T> Retry.Decision<T> decision();

    default <T> T executeCallable(Callable<T> callable) throws Exception {
        return decorateCallable(this, callable).call();
    }

    static <T> Callable<T> decorateCallable(Retry retry, Callable<T> callable) {
        return () -> {
            Retry.Decision<T> decision = retry.decision();
            do {
                try {
                    T result = callable.call();
                    final boolean isContinue = decision.isContinue();
                    if (isContinue) {
                        decision.onComplete();
                        return result;
                    }
                } catch (Exception e) {
                    decision.onException(e);
                }
            } while (true);
        };
    }

    static <T,R> Function<T, R> decorateFunction(Retry retry, Function<T, R> function) {
        return (T t) -> {
            Retry.Decision<R> decision = retry.decision();
            do {
                try {
                    decision.onComplete();
                    return function.apply(t);
                } catch (RuntimeException runtimeException) {
                    if (decision.isContinue()) {
                        decision.onRuntimeException(runtimeException);
                    }
                }
            } while (true);
        };
    }


    interface Decision<T> {
        void onComplete();

        boolean isContinue();

        void onException(Exception exception) throws Exception;

        void onRuntimeException(RuntimeException runtimeException);
    }
}
