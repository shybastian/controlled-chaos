package edu.seb.chaos.now.http.config;

import edu.seb.chaos.now.http.components.Assault;
import edu.seb.chaos.now.http.components.ClientInterceptor;
import edu.seb.chaos.now.http.rest.HttpAssaultController;
import edu.seb.chaos.now.http.util.AssaultUtils;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;


@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties({HttpProperties.class, AssaultProperties.class})
public class HttpConfiguration {

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
    public AssaultUtils utils() {
        return new AssaultUtils();
    }

    @Bean()
    public Assault assault(HttpSettings httpSettings) {
        return new Assault(httpSettings);
    }

    @Bean
    public ClientInterceptor clientInterceptor(AssaultUtils utils, Assault assault) {
        return new ClientInterceptor(utils, assault);
    }

    @Bean
    public RestTemplate restTemplate(ClientInterceptor... interceptors) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Arrays.asList(interceptors));
        return restTemplate;
    }

    @Bean
    @ConditionalOnAvailableEndpoint
    public HttpAssaultController httpAssaultController() {
        return new HttpAssaultController(httpSettings());
    }
}
