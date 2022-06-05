package edu.seb.chaos.circuitbreaker.components;

import java.util.concurrent.Callable;

public interface CircuitBreaker {

    CircuitState getState();
    <T> Callable<T> decorateCallable(Callable<T> callable);
}
