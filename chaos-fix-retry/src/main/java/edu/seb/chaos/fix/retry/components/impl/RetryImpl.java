package edu.seb.chaos.fix.retry.components.impl;

import edu.seb.chaos.fix.retry.components.Retry;
import edu.seb.chaos.fix.retry.config.RetryConfiguration;
import edu.seb.chaos.fix.retry.exception.RetriesExceededException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@Slf4j
public class RetryImpl implements Retry {

    private final RetryConfiguration configuration;
    private final int maxAttempts;

    private final List<Class<? extends Throwable>> retryExceptions;
    private final List<Class<? extends Throwable>> ignoreExceptions;

    public RetryImpl(RetryConfiguration configuration) {
        this.configuration = configuration;
        this.maxAttempts = this.configuration.getMaxAttempts();
        this.retryExceptions = this.configuration.getRetryExceptions();
        this.ignoreExceptions = this.configuration.getIgnoreExceptions();
    }

    @Override
    public <T> Callable<T> decorateCallable(Callable<T> callable) {
        return () -> {
            Retry.Decision decision = new DecisionImpl();
            do {
                try {
                    T result = callable.call();
                    return result;
                } catch (Exception e) {
                    decision.onException(e);
                }
            } while (true);
        };
    }

    public final class DecisionImpl implements Retry.Decision {
        private Integer attempts = 0;
        private DecisionImpl() {}

        @Override
        public void onException(Exception exception) throws Exception {
            if (isIgnoreException(exception)) {
                this.sleep();
                return;
            }
            if (isRetryException(exception)) {
                log.info("Encountered retryable exception of type: {} in retry #{}.",
                        exception.getClass().getSimpleName(), attempts);
                attempts++;
                if (attempts > maxAttempts && configuration.isFailAfterMaxAttempts()) {
                    throw RetriesExceededException.throwRetryExceededOnExceptionException(maxAttempts);
                }
                this.sleep();
            } else {
                // Exception was not in retry/ignore list so throw it
                throw exception;
            }
        }

        private boolean isIgnoreException(Exception exception) {
            for (Class<? extends Throwable> clazz : ignoreExceptions) {
                if (clazz.isAssignableFrom(exception.getClass())) return true;
            }
            return false;
        }

        private boolean isRetryException(Exception exception) {
            for (Class<? extends Throwable> clazz: retryExceptions) {
                if (clazz.isAssignableFrom(exception.getClass())) return true;
            }
            return false;
        }

        private void sleep() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                //
            }
        }
    }
}
