package edu.seb.chaos.fix.retry.config;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;


@Slf4j
@NoArgsConstructor
public class RetryConfiguration {
    public static final int DEFAULT_MAX_ATTEMPTS = 3;
    public static final boolean DEFAULT_FAIL_AFTER_MAX_ATTEMPTS = true;
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
    private boolean failAfterMaxAttempts = DEFAULT_FAIL_AFTER_MAX_ATTEMPTS;

    private Class<? extends Throwable>[] retryExceptions = new Class[0];
    private Class<? extends Throwable>[] ignoreExceptions = new Class[0];

    private static final Predicate<Throwable> DEFAULT_RECORD_FAILURE_PREDICATE = throwable -> true;

    @Nullable
    private Predicate<Throwable> retryOnExceptionPredicate;

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public boolean isFailAfterMaxAttempts() {
        return failAfterMaxAttempts;
    }

    @Nullable
    public Predicate<Throwable> getRetryOnExceptionPredicate() {
        return retryOnExceptionPredicate;
    }

    public static <T> Builder<T> createConfiguration() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
        private boolean failAfterMaxAttempts = DEFAULT_FAIL_AFTER_MAX_ATTEMPTS;
        @Nullable
        private Predicate<Throwable> retryOnExceptionPredicate;

        private Class<? extends Throwable>[] retryExceptions = new Class[0];
        private Class<? extends Throwable>[] ignoreExceptions = new Class[0];

        public Builder() {}

        public RetryConfiguration build() {
            RetryConfiguration configuration = new RetryConfiguration();
            configuration.maxAttempts = maxAttempts;
            configuration.failAfterMaxAttempts = failAfterMaxAttempts;
            configuration.retryExceptions = retryExceptions;
            configuration.ignoreExceptions = ignoreExceptions;
            configuration.retryOnExceptionPredicate = createExceptionPredicate();
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
        public final Builder<T> setRetryExceptions(@Nullable Class<? extends Throwable>... retryExceptions) {
            this.retryExceptions = retryExceptions != null ? retryExceptions : new Class[0];
            return this;
        }

        @SafeVarargs
        public final Builder<T> setIgnoreExceptions(@Nullable  Class<? extends Throwable>... ignoreExceptions) {
            this.ignoreExceptions = ignoreExceptions != null ? ignoreExceptions : new Class[0];
            return this;
        }

        private Predicate<Throwable> createExceptionPredicate() {
            return createRetryOnExceptionPredicate()
                    .and(PredicateCreator.createNegatedExceptionsPredicate(ignoreExceptions)
                            .orElse(DEFAULT_RECORD_FAILURE_PREDICATE));
        }

        private Predicate<Throwable> createRetryOnExceptionPredicate() {
            return PredicateCreator.createExceptionsPredicate(retryExceptions)
                    .map(predicate -> retryOnExceptionPredicate != null ? predicate.or(retryOnExceptionPredicate) : predicate)
                    .orElseGet(() -> retryOnExceptionPredicate != null ? retryOnExceptionPredicate : DEFAULT_RECORD_FAILURE_PREDICATE);
        }

        private static class PredicateCreator {
            @SafeVarargs
            public static Optional<Predicate<Throwable>> createExceptionsPredicate(
                    Class<? extends Throwable>... recordExceptions) {
                return exceptionPredicate(recordExceptions);
            }

            @SafeVarargs
            public static Optional<Predicate<Throwable>> createNegatedExceptionsPredicate(
                    Class<? extends Throwable>... ignoreExceptions) {
                return exceptionPredicate(ignoreExceptions)
                        .map(Predicate::negate);
            }

            private static Optional<Predicate<Throwable>> exceptionPredicate(
                    Class<? extends Throwable>[] recordExceptions) {
                return Arrays.stream(recordExceptions)
                        .distinct()
                        .map(PredicateCreator::makePredicate)
                        .reduce(Predicate::or);
            }

            private static Predicate<Throwable> makePredicate(Class<? extends Throwable> exClass) {
                return (Throwable e) -> exClass.isAssignableFrom(e.getClass());
            }
        }
    }
}
