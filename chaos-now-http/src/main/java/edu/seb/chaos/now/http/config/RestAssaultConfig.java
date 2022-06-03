package edu.seb.chaos.now.http.config;

import edu.seb.chaos.now.http.components.Assault;
import edu.seb.chaos.now.http.components.ClientInterceptor;
import edu.seb.chaos.now.http.util.AssaultUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestAssaultConfig {

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
    public RestTemplate restTemplate(ClientInterceptor interceptor) {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        return restTemplate;
    }
}
