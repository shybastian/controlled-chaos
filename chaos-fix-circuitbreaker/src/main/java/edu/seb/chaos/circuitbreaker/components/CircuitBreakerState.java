package edu.seb.chaos.circuitbreaker.components;

public interface CircuitBreakerState {

    void acquirePermission();
    /**
     * False - no change
     * True - transition to open
     * @param throwable
     * @return
     */
    void onError(Throwable throwable);
    void onSuccess();
}
