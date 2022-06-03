package edu.seb.chaos.circuitbreaker.components;

import java.util.concurrent.Callable;

public interface CircuitBreaker {

    CircuitState getState();
    void acquirePermission();
    void onSuccess();
    void onError(Throwable throwable);
    void transitionToClosedState();
    void transitionToHalfOpenState();
    void transitionToOpenState();

    default <T> T executeCallable(Callable<T> callable) throws Exception {
        return decorateCallable(this, callable).call();
    }

    static <T> Callable<T> decorateCallable(CircuitBreaker circuitBreaker, Callable<T> callable) {
        return () -> {
            circuitBreaker.acquirePermission();
            try {
                T result = callable.call();
                circuitBreaker.onSuccess();
                return result;
            } catch (Exception exception) {
                circuitBreaker.onError(exception.getCause());
                throw exception;
            }
        };
    }
}
