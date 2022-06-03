package edu.seb.chaos.now.assault.assaults;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KillAssault implements Assault {
    @Override
    public void attack() {
        this.killApplication();
    }

    private void killApplication() {
        try {
            log.info("Chaos-Now :-: KillSwitch");
            System.exit(0);
        } catch (Exception e) {
            log.info("Could not Kill application... Reason: {}", e.getMessage());
        }
    }
}
