package edu.seb.chaos.now.assault.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "chaos.now.assault.assaults")
public class AssaultProperties {
    private boolean exceptionsActive = false;
    private int probabilityPercentage = 50;

    // Will always be RunTimeException for now!!
    @NestedConfigurationProperty
    private AssaultException exception;

    public AssaultException getException() {
        return exception == null ? new AssaultException() : exception;
    }

    public void setException(AssaultException exception) {
        this.exception = exception;
    }

    public void setProbabilityPercentage(int probabilityPercentage) {
        this.probabilityPercentage = probabilityPercentage;
    }
}
