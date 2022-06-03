package edu.seb.chaos.now.assault.assaults;

import edu.seb.chaos.now.assault.config.AssaultException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ExceptionAssault implements Assault {
    private final AssaultException assaultException;

    public ExceptionAssault(AssaultException assaultException) {
        this.assaultException = assaultException;
    }

    @Override
    public void attack() {
        this.assaultException.throwExceptionInstance();
    }
}
