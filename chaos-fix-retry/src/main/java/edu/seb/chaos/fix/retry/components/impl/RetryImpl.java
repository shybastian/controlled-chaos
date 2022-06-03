package edu.seb.chaos.fix.retry.components.impl;

import edu.seb.chaos.fix.retry.components.Retry;
import edu.seb.chaos.fix.retry.config.RetryConfiguration;
import edu.seb.chaos.fix.retry.exception.RetriesExceededException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

@Slf4j
public class RetryImpl<T> implements Retry {

    private final RetryConfiguration properties;
    private final int maxAttempts;
    private final Predicate<Throwable> exceptionPredicate;

    public RetryImpl(RetryConfiguration properties) {
        this.properties = properties;
        this.maxAttempts = this.properties.getMaxAttempts();
        this.exceptionPredicate = this.properties.getRetryOnExceptionPredicate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Decision<T> decision() {
        return new DecisionImpl();
    }

    public final class DecisionImpl implements Retry.Decision<T> {
        private Integer attempts = 0;
        private DecisionImpl() {}

        @Override
        public void onComplete() {
            if (attempts > maxAttempts) {
                throw RetriesExceededException.throwRetryExceededOnCompleteException(maxAttempts);
            }
            log.info("Retry completed in: {} attempts.", attempts);
        }

        @Override
        public boolean isContinue() {
            return true;
        }

        @Override
        public void onException(Exception exception) throws Exception {
            if (exceptionPredicate != null && exceptionPredicate.test(exception)) {
                log.info("Encountered retryable exception of type: {} in retry #{}.",
                        exception.getClass().getSimpleName(), attempts);
                attempts++;
                if (attempts > maxAttempts && properties.isFailAfterMaxAttempts()) {
                    throw RetriesExceededException.throwRetryExceededOnExceptionException(maxAttempts);
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    //
                }
            } else {
                // Exception was not in retry/ignore Exceptions list
                throw exception;
            }
        }

        @Override
        public void onRuntimeException(RuntimeException exception) {
            if (exceptionPredicate != null && exceptionPredicate.test(exception)) {
                log.info("Encountered retryable exception in retry #{}. Incrementing and retrying.", attempts);
                attempts++;
            } else {
                throw exception;
            }
        }
    }
}
