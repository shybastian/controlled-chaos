package edu.seb.chaos.fix.retry.components;


import java.util.concurrent.Callable;

public interface Retry {

    <T> Callable<T> decorateCallable(Callable<T> callable);

    interface Decision {
        void onException(Exception exception) throws Exception;
    }
}
