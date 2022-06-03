package edu.seb.chaos.circuitbreaker.config;

public class CircuitBreakerConfiguration {
    public static final int DEFAULT_FAIL_THRESHOLD = 5;
    private int failThreshold = DEFAULT_FAIL_THRESHOLD;

    public static final int DEFAULT_WAIT_SECONDS = 5;
    private int waitSeconds = DEFAULT_WAIT_SECONDS;

    public CircuitBreakerConfiguration() {}

    public int getFailThreshold() {
        return this.failThreshold;
    }

    public int getWaitSeconds() {
        return this.waitSeconds;
    }

    public static Builder configure() {
        return new Builder();
    }


    public static class Builder {
        private int failThreshold = DEFAULT_FAIL_THRESHOLD;
        private int waitSeconds = DEFAULT_WAIT_SECONDS;

        public Builder() {}

        public Builder failureRateThreshold(int failureRateThreshold) {
            if (failureRateThreshold <= 0 || failureRateThreshold > 100) {
                throw new IllegalArgumentException(
                        "failureRateThreshold must be between 1 and 100");
            }
            this.failThreshold = failureRateThreshold;
            return this;
        }

        public Builder waitSeconds(int waitSeconds) {
            if (waitSeconds <= 0 || waitSeconds > 3600) {
                throw new IllegalArgumentException(
                        "failureRateThreshold must be between 0 and 3600");
            }
            this.waitSeconds = waitSeconds;
            return this;
        }

        public CircuitBreakerConfiguration build() {
            CircuitBreakerConfiguration configuration = new CircuitBreakerConfiguration();
            configuration.failThreshold = failThreshold;
            configuration.waitSeconds = waitSeconds;
            return configuration;
        }
    }

}
