package edu.seb.chaos.circuitbreaker.exception;

public class CircuitBreakerException extends RuntimeException{

    private CircuitBreakerException(String message) {
        super(message);
    }

    public static CircuitBreakerException throwCircuitBreakerIsOpen() {
        String message = "Call has not been performed. CircuitBreaker is OPEN and awaiting timeout.";
        return new CircuitBreakerException(message);
    }
}
