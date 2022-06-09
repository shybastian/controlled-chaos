package edu.seb.chaos.now.assault.components;

import edu.seb.chaos.now.assault.assaults.Assault;
import edu.seb.chaos.now.assault.assaults.ExceptionAssault;
import edu.seb.chaos.now.assault.config.AssaultException;
import edu.seb.chaos.now.assault.config.Settings;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@Slf4j
public class ExceptionAssaultInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAssaultInvoker.class);

    private final Settings settings;
    private final Assault assault;

    private final Integer LOW = 1;
    private final Integer HIGH = 100;

    public ExceptionAssaultInvoker(Settings settings) {
        this.settings = settings;
        this.assault = new ExceptionAssault(new AssaultException());
    }

    public void doSomeChaos(String name) {
        if (shouldAttack()) {
            LOGGER.info("Doing some chaos with: {}.", name);
            this.assault.attack();
        }
    }

    private boolean shouldAttack() {
        int threshold = 50;
        if (this.settings.getAssaultProperties() != null) {
            threshold = this.settings.getAssaultProperties().getProbabilityPercentage();
        }
        Random random = new Random();
        int roll = random.nextInt((HIGH - LOW) + LOW);
        boolean isAttacking =  roll > threshold;
        log.info("Chaos Now Rolled: {}. Threshold is: {}", roll, threshold);
        return this.settings.getProperties().isEnabled() && this.settings.getAssaultProperties().isExceptionsActive() && isAttacking;
    }
}
