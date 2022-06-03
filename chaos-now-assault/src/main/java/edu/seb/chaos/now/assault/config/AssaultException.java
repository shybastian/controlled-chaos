package edu.seb.chaos.now.assault.config;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class AssaultException {
    @SneakyThrows
    public void throwExceptionInstance() {
        throw getThrowable();
    }

    private Throwable getThrowable() {
        return new RuntimeException("Chaos-Now : RuntimeException");
    }
}
