package edu.seb.chaos.fix.retry.exception;

public class RetriesExceededException extends RuntimeException {

    private RetriesExceededException(String message){
        super(message);
    }

    public static RetriesExceededException throwRetryExceededOnCompleteException(int maxAttempts) {
        String message = "Retries exceeded maximum amount of attempts which was: " + maxAttempts;
        return new RetriesExceededException(message);
    }

    public static RetriesExceededException throwRetryExceededOnExceptionException(int maxAttempts) {
        String message = "Retries exceeded maximum amount of attempts which was: " + maxAttempts + ". Furthermore " +
                "received too many exceptions and giving up.";
        return new RetriesExceededException(message);
    }
}
