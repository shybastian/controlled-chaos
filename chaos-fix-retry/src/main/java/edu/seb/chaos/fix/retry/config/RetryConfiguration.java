package edu.seb.chaos.fix.retry.config;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@Slf4j
@NoArgsConstructor
public class RetryConfiguration {
    public static final int DEFAULT_MAX_ATTEMPTS = 3;
    public static final boolean DEFAULT_FAIL_AFTER_MAX_ATTEMPTS = true;
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
    private boolean failAfterMaxAttempts = DEFAULT_FAIL_AFTER_MAX_ATTEMPTS;

    private List<Class<? extends Throwable>> retryExceptions = new ArrayList<>();
    private List<Class<? extends Throwable>> ignoreExceptions = new ArrayList<>();

    public List<Class<? extends Throwable>> getRetryExceptions() {
        return retryExceptions;
    }

    public List<Class<? extends Throwable>> getIgnoreExceptions() {
        return ignoreExceptions;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public boolean isFailAfterMaxAttempts() {
        return failAfterMaxAttempts;
    }

    public static <T> Builder<T> createConfiguration() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
        private boolean failAfterMaxAttempts = DEFAULT_FAIL_AFTER_MAX_ATTEMPTS;
        @SuppressWarnings(value = "unchecked")
        private Class<? extends Throwable>[] retryExceptions = new Class[0];
        @SuppressWarnings(value = "unchecked")
        private Class<? extends Throwable>[] ignoreExceptions = new Class[0];

        public Builder() {}

        public RetryConfiguration build() {
            RetryConfiguration configuration = new RetryConfiguration();
            configuration.maxAttempts = maxAttempts;
            configuration.failAfterMaxAttempts = failAfterMaxAttempts;
            configuration.retryExceptions = Arrays.asList(retryExceptions);
            configuration.ignoreExceptions = Arrays.asList(ignoreExceptions);
            return configuration;
        }

        public Builder<T> setMaxAttempts(int maxAttempts) {
            if (maxAttempts < 1) {
                this.maxAttempts = DEFAULT_MAX_ATTEMPTS;
                throw new IllegalArgumentException("ChaosFix - Retry - Max Attempts is lower than 1. Defaulting ...");

            }
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder<T> setFailAfterMaxAttempts(boolean failAfterMaxAttempts) {
            this.failAfterMaxAttempts = failAfterMaxAttempts;
            return this;
        }

        @SafeVarargs
        @SuppressWarnings(value = "unchecked")
        public final Builder<T> setRetryExceptions(@Nullable Class<? extends Throwable>... retryExceptions) {
            this.retryExceptions = retryExceptions != null ? retryExceptions : new Class[0];
            return this;
        }

        @SafeVarargs
        @SuppressWarnings(value = "unchecked")
        public final Builder<T> setIgnoreExceptions(@Nullable  Class<? extends Throwable>... ignoreExceptions) {
            this.ignoreExceptions = ignoreExceptions != null ? ignoreExceptions : new Class[0];
            return this;
        }
    }
}
