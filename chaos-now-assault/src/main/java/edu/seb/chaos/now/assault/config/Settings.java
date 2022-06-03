package edu.seb.chaos.now.assault.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

    @NotNull
    private Properties properties;

    @NotNull
    private AssaultProperties assaultProperties;
}
