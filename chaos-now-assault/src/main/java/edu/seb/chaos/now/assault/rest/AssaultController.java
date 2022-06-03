package edu.seb.chaos.now.assault.rest;

import edu.seb.chaos.now.assault.config.AssaultProperties;
import edu.seb.chaos.now.assault.config.Properties;
import edu.seb.chaos.now.assault.config.Settings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.*;

@RestControllerEndpoint(id = "chaos-now-assault", enableByDefault = true)
@RestController
@CrossOrigin
@RequestMapping("/assault/exceptions")
@Slf4j
public class AssaultController {
    private final Settings settings;

    public AssaultController(Settings settings) {
        this.settings = settings;
    }

    @GetMapping("/isEnabled")
    public Boolean isEnabled() {
        Properties properties = this.settings.getProperties();
        AssaultProperties aProperties = this.settings.getAssaultProperties();
        return properties.isEnabled() && aProperties.isExceptionsActive();
    }

    @PostMapping("/isEnabled/{isAttackEnabled}")
    public void setAttackEnabled(@PathVariable Boolean isAttackEnabled) {
        log.info("Is Attack Enabled? {}", isAttackEnabled);
        Properties properties = this.settings.getProperties();
        properties.setEnabled(isAttackEnabled);
        AssaultProperties aProperties = this.settings.getAssaultProperties();
        aProperties.setExceptionsActive(isAttackEnabled);
    }

    @GetMapping("/threshold")
    public Integer getThreshold() {
        return this.settings.getAssaultProperties().getProbabilityPercentage();
    }

    @PostMapping("/threshold/{threshold}")
    public void setThreshold(@PathVariable Integer threshold) {
        AssaultProperties assaultProperties = this.settings.getAssaultProperties();
        assaultProperties.setProbabilityPercentage(threshold);
    }
}
