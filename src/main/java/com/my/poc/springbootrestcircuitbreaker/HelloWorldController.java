package com.my.poc.springbootrestcircuitbreaker;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    @Value("${downstream-rest-api-url}")
    private String downstreamRestApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping("/message")
    @SneakyThrows
    public ResponseEntity<String> getMessage() {

        RequestEntity requestEntity = new RequestEntity<>(
          HttpMethod.GET,
          new URI(downstreamRestApiUrl + "/message")
        );

        String message = circuitBreakerFactory.create("downstream-service-id-or-name").run(() ->
                (restTemplate.exchange(requestEntity, String.class).getBody()),
                throwable -> "This is fallback message because call to downstream was not successful");

        return ResponseEntity.ok(message);
    }
}
