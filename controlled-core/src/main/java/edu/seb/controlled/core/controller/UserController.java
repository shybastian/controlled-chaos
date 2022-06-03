package edu.seb.controlled.core.controller;

import edu.seb.chaos.circuitbreaker.components.CircuitState;
import edu.seb.chaos.circuitbreaker.exception.CircuitBreakerException;
import edu.seb.chaos.fix.retry.components.Retry;
import edu.seb.chaos.fix.retry.config.RetryConfiguration;
import edu.seb.chaos.circuitbreaker.components.CircuitBreaker;
import edu.seb.chaos.circuitbreaker.components.impl.CircuitBreakerImpl;
import edu.seb.chaos.circuitbreaker.config.CircuitBreakerConfiguration;
import edu.seb.chaos.fix.retry.exception.RetriesExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    private int id;
    private final Callable<ResponseEntity<Object>> toCall;
    //private static final String URL = "Controlleduser-env.eba-kcfnqjzn.eu-central-1.elasticbeanstalk.com/user/";
    private static final String URL = "http://localhost:8080/user/";

    private CircuitBreaker circuitBreaker = null;

    private UserController() {
        this.toCall = () -> restTemplate.getForEntity(URL + id, Object.class);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getByIdNormal(@PathVariable int id) {
        this.id = id;
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(URL + id, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("User-Service temporary unavailable + cause: " + e.getMessage());
        }
    }

    @GetMapping("/retry/{id}")
    public ResponseEntity<Object> getByIdRetry(@PathVariable int id) throws Exception {
        Callable<ResponseEntity<Object>> callable = () -> restTemplate.getForEntity(URL + id, Object.class);

        RetryConfiguration configuration = RetryConfiguration.createConfiguration()
                .setMaxAttempts(3)
                .setFailAfterMaxAttempts(true)
                .setRetryExceptions(RuntimeException.class)
                .build();

        try {
            ResponseEntity<Object> result = Retry.decorateCallable(Retry.of(configuration), callable).call();
            return ResponseEntity.ok(result);
        } catch (RetriesExceededException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getMessage());
        }
    }

    @GetMapping("/circuit/{id}")
    public ResponseEntity<Object> getByIdCircuit(@PathVariable int id) {
        if (circuitBreaker == null) {
            init();
        }
        try {
            this.id = id;
            ResponseEntity<Object> result = CircuitBreaker.decorateCallable(circuitBreaker, this.toCall).call();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            if (e instanceof CircuitBreakerException) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body("CircuitBreaker Opened because of the unavailable service. Message: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("User-Service temporary unavailable + cause: " + e);
        }
    }

    @GetMapping("/circuit")
    public ResponseEntity<CircuitState> getCircuitState() {
        if (circuitBreaker == null){
            return ResponseEntity.ok().body(CircuitState.DEACTIVATED);
        }
        return ResponseEntity.ok().body(circuitBreaker.getState());
    }

    private void init() {
        CircuitBreakerConfiguration configuration = CircuitBreakerConfiguration.configure()
                .waitSeconds(5).failureRateThreshold(5).build();
        this.circuitBreaker = new CircuitBreakerImpl(configuration);
    }
}
