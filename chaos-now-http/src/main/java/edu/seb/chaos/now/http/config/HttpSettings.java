package edu.seb.chaos.now.http.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpSettings {

    @NotNull
    private HttpProperties httpProperties;

    @NotNull
    private AssaultProperties assaultProperties;
}
