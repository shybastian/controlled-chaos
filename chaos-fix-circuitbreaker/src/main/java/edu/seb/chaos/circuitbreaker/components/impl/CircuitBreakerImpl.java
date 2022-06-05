package edu.seb.chaos.circuitbreaker.components.impl;

import edu.seb.chaos.circuitbreaker.components.CircuitBreaker;
import edu.seb.chaos.circuitbreaker.components.CircuitBreakerState;
import edu.seb.chaos.circuitbreaker.components.CircuitState;
import edu.seb.chaos.circuitbreaker.config.CircuitBreakerConfiguration;
import edu.seb.chaos.circuitbreaker.exception.CircuitBreakerException;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class CircuitBreakerImpl implements CircuitBreaker {
    private final CircuitBreakerConfiguration configuration;
    private final AtomicReference<CircuitBreakerState> state;
    private final AtomicReference<CircuitState> circuitState;

    public CircuitBreakerImpl(CircuitBreakerConfiguration configuration) {
        this.configuration = configuration;
        this.state = new AtomicReference<>(new ClosedState(this.configuration.getFailThreshold()));
        this.circuitState = new AtomicReference<>(CircuitState.CLOSED);
    }
    @Override
    public CircuitState getState() {
        return this.circuitState.get();
    }

    @Override
    public <T> Callable<T> decorateCallable(Callable<T> callable) {
        return () -> {
            this.acquirePermission();
            try {
                T result = callable.call();
                this.onSuccess();
                return result;
            } catch (Exception exception) {
                this.onError(exception.getCause());
                throw exception;
            }
        };
    }

    private void acquirePermission() {
        this.state.get().acquirePermission();
    }

    private void onSuccess() {
        this.state.get().onSuccess();
    }

    private void onError(Throwable throwable) {
        this.state.get().onError(throwable);
    }

    private void transitionToClosedState() {
        this.state.getAndUpdate(newState -> new ClosedState(this.configuration.getFailThreshold()));
        this.circuitState.getAndUpdate(newSTate -> CircuitState.CLOSED);
    }

    private void transitionToHalfOpenState() {
        this.state.getAndUpdate(newState -> new HalfOpenState(this.configuration.getFailThreshold()));
        this.circuitState.getAndUpdate(newSTate -> CircuitState.HALF_OPEN);
    }

    private void transitionToOpenState() {
        Instant waitUntil = Instant.now().plusSeconds(this.configuration.getWaitSeconds());
        log.info("CircuitBreaker transitioning to OPEN State, blocking calls. Will be HALF-OPEN at: {}", waitUntil.toString());
        this.state.getAndUpdate(newState -> new OpenState(waitUntil));
        this.circuitState.getAndUpdate(newState -> CircuitState.OPEN);
    }

    private class ClosedState implements CircuitBreakerState {
        private final int maxFailedAttempts;
        private final AtomicInteger failedAttempts;

        public ClosedState(int failThreshold) {
            this.maxFailedAttempts = failThreshold;
            this.failedAttempts = new AtomicInteger(0);
        }

        @Override
        public void acquirePermission() {
            log.info("Acquired Permission in state CLOSED. Proceed!");
        }

        @Override
        public void onError(Throwable throwable) {
            int current = failedAttempts.getAndIncrement();
            log.info("Call #{} failed in CLOSED state! Threshold until OPEN State is: #{}", current, maxFailedAttempts);
            if (current >= maxFailedAttempts) {
                log.info("Call failed repeatedly ... Transitioning to State OPEN.");
                transitionToOpenState();
            }
        }

        @Override
        public void onSuccess() {
            failedAttempts.getAndUpdate(current -> current < 1 ? current = 0 : current--);
        }
    }

    private class HalfOpenState implements CircuitBreakerState {
        private final int maxAttempts;
        private final AtomicInteger failedAttempts;
        private final AtomicInteger successfulAttempts;

        public HalfOpenState(int failThreshold) {
            this.maxAttempts = Math.toIntExact(failThreshold / 2);
            this.failedAttempts = new AtomicInteger(0);
            this.successfulAttempts = new AtomicInteger(0);
        }

        @Override
        public void acquirePermission() {
            log.info("Acquiring Permission in HALF-OPEN state!");
        }

        /**
         * On erroneous call, increment failedAttempts. If the value is bigger than the maxAttempts,
         * transition to OpenState.
         * @param throwable exception that was thrown during call.
         */
        @Override
        public void onError(Throwable throwable) {
            int current = failedAttempts.getAndIncrement();
            log.info("Failed call #{} in HALF-OPEN State. Threshold is {} until OPEN state.", current, maxAttempts);
            if (current >= maxAttempts) {
                log.info("Too many failed calls in HALF-OPEN state. Transitioning to OPEN!");
                transitionToOpenState();
            }
        }

        /**
         * On successful call, increment successfulAttempt. If the value is bigger than the maxAttempts,
         * transition to Closed state.
         */
        @Override
        public void onSuccess() {
            int current = successfulAttempts.getAndIncrement();
            log.info("Successful call #{} in HALF-OPEN State. Threshold is {} until CLOSED state.", current, maxAttempts);
            if (current >= maxAttempts) {
                log.info("Multiple successful attempts in HALF-OPEN state. Transitioning to CLOSED!");
                transitionToClosedState();
            }
        }
    }

    private class OpenState implements CircuitBreakerState {
        private final Instant waitUntil;
        public OpenState(Instant waitUntil) {
            this.waitUntil = waitUntil;
        }

        /**
         * Wait for current time to surpass "waitUntil". If it hasn't, blocks calls.
         * If it has passed, transitions to HalfOpen state.
         */
        @Override
        public void acquirePermission() {
            log.info("Acquiring Permission in OPEN State ...");
            if (Instant.now().isAfter(waitUntil)) {
                log.info("Wait Time has passed ...");
                log.info("Permission granted for test call. Transitioning to HALF-OPEN!");
                transitionToHalfOpenState();
            } else {
                log.info("Blocking call because CircuitBreaker is OPEN ...");
                throw CircuitBreakerException.throwCircuitBreakerIsOpen();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            // No Error since AcquirePermission already blocks calls
        }

        @Override
        public void onSuccess() {
            // No Success since AcquirePermission already blocks calls
        }
    }
}


