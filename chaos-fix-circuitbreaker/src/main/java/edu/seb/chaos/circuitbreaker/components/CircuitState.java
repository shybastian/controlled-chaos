package edu.seb.chaos.circuitbreaker.components;

public enum CircuitState {
    OPEN,
    CLOSED,
    HALF_OPEN,
    DEACTIVATED;
}
