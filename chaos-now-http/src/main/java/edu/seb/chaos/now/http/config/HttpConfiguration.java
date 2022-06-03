package edu.seb.chaos.now.http.config;

import edu.seb.chaos.now.http.rest.HttpAssaultController;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties({HttpProperties.class, AssaultProperties.class})
@Import(RestAssaultConfig.class)
public class HttpConfiguration implements WebMvcConfigurer {

    private final HttpProperties httpProperties;
    private final AssaultProperties assaultProperties;

    public HttpConfiguration(HttpProperties httpProperties, AssaultProperties assaultProperties) {
        this.httpProperties = httpProperties;
        this.assaultProperties = assaultProperties;
    }

    @Bean
    public HttpSettings httpSettings() {
        return new HttpSettings(httpProperties, assaultProperties);
    }

    @Bean
    @ConditionalOnAvailableEndpoint
    public HttpAssaultController httpAssaultController() {
        return new HttpAssaultController(httpSettings());
    }
}
