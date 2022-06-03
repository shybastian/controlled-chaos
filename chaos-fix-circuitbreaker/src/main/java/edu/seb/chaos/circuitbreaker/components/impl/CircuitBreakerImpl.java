package edu.seb.chaos.circuitbreaker.components.impl;

import edu.seb.chaos.circuitbreaker.components.CircuitBreaker;
import edu.seb.chaos.circuitbreaker.components.CircuitBreakerState;
import edu.seb.chaos.circuitbreaker.components.CircuitState;
import edu.seb.chaos.circuitbreaker.config.CircuitBreakerConfiguration;
import edu.seb.chaos.circuitbreaker.exception.CircuitBreakerException;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
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
    public void acquirePermission() {
        this.state.get().acquirePermission();
    }

    @Override
    public void onSuccess() {
        this.state.get().onSuccess();
    }

    @Override
    public void onError(Throwable throwable) {
        this.state.get().onError(throwable);
    }

    @Override
    public void transitionToClosedState() {
        this.state.getAndUpdate(newState -> new ClosedState(this.configuration.getFailThreshold()));
        this.circuitState.getAndUpdate(newSTate -> CircuitState.CLOSED);
    }

    @Override
    public void transitionToHalfOpenState() {
        this.state.getAndUpdate(newState -> new HalfOpenState(this.configuration.getFailThreshold()));
        this.circuitState.getAndUpdate(newSTate -> CircuitState.HALF_OPEN);
    }

    @Override
    public void transitionToOpenState() {
        Instant waitUntil = Instant.now().plusSeconds(this.configuration.getWaitSeconds());
        log.info("CircuitBreaker transitioning to OPEN State, blocking calls. Will be HALF-OPEN at: {}", waitUntil.toString());
        this.state.getAndUpdate(newState -> new OpenState(waitUntil));
        this.circuitState.getAndUpdate(newSTate -> CircuitState.OPEN);
    }

    public class ClosedState implements CircuitBreakerState {
        private final int permittedNumberOfAttempts;
        private final AtomicInteger currentNumberOfAttempts;

        public ClosedState(int failThreshold) {
            this.permittedNumberOfAttempts = failThreshold;
            this.currentNumberOfAttempts = new AtomicInteger(0);
        }

        @Override
        public void acquirePermission() {
            log.info("Acquired Permission in state CLOSED. Proceed!");
        }

        @Override
        public void onError(Throwable throwable) {
            int current = currentNumberOfAttempts.getAndIncrement();
            log.info("Call #{} failed in CLOSED state! Threshold until OPEN State is: #{}", current, permittedNumberOfAttempts);
            if (current >= permittedNumberOfAttempts) {
                log.info("Call failed repeatedly ... Transitioning to State OPEN.");
                transitionToOpenState();
            }
        }

        @Override
        public void onSuccess() {
            currentNumberOfAttempts.getAndUpdate(current -> current < 1 ? current = 0 : current--);
        }
    }

    private class HalfOpenState implements CircuitBreakerState {
        private final int permittedNumberOfAttempts;
        private final AtomicInteger currentNumberOfAttempts;
        private final AtomicInteger currentNumberOfSuccessfulAttempts;

        public HalfOpenState(int failThreshold) {
            this.permittedNumberOfAttempts = Math.toIntExact(failThreshold / 2);
            this.currentNumberOfAttempts = new AtomicInteger(0);
            this.currentNumberOfSuccessfulAttempts = new AtomicInteger(0);
        }

        @Override
        public void acquirePermission() {
            log.info("Acquiring Permission in HALF-OPEN state!");
        }

        /**
         * On erroneous call, increment currentAttempt. If the value is bigger than the permittedNumberOfAttempts,
         * transition to OpenState.
         * @param throwable exception that was thrown during call.
         */
        @Override
        public void onError(Throwable throwable) {
            int current = currentNumberOfAttempts.getAndIncrement();
            log.info("Failed call #{} in HALF-OPEN State. Threshold is {} until OPEN state.", current, permittedNumberOfAttempts);
            if (current >= permittedNumberOfAttempts) {
                log.info("Too many failed calls in HALF-OPEN state. Transitioning to OPEN!");
                transitionToOpenState();
            }
        }

        /**
         * On successful call, increment successfulAttempt. If the value is bigger than the permittedNumberOfAttempts,
         * transition to Closed state.
         */
        @Override
        public void onSuccess() {
            int current = currentNumberOfSuccessfulAttempts.getAndIncrement();
            log.info("Successful call #{} in HALF-OPEN State. Threshold is {} until CLOSED state.", current, permittedNumberOfAttempts);
            if (current >= permittedNumberOfAttempts) {
                log.info("Multiple successful attempts in HALF-OPEN state. Transitioning to CLOSED!");
                transitionToClosedState();
            }
        }
    }

    public class OpenState implements CircuitBreakerState {
        private int attempts;
        private final Instant waitUntil;
        public OpenState(Instant waitUntil) {
            this.attempts = 0;
            this.waitUntil = waitUntil;
        }

        @Override
        public void acquirePermission() {
            log.info("Acquiring Permission in OPEN State ...");
            if (Instant.now().isAfter(waitUntil)) {
                log.info("Permission granted for test call. Transitioning to HALF-OPEN!");
                transitionToHalfOpenState();
            } else {
                attempts++;
                log.info("Circuit is in OPEN State! Call was attempt #{}", attempts);
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


