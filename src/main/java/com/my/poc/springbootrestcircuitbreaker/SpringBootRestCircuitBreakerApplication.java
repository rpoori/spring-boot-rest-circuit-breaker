package com.my.poc.springbootrestcircuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@SpringBootApplication
public class SpringBootRestCircuitBreakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestCircuitBreakerApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	CircuitBreakerFactory circuitBreakerFactory() {
		Resilience4JCircuitBreakerFactory factory = new Resilience4JCircuitBreakerFactory();

		factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(
						TimeLimiterConfig.custom()
								.timeoutDuration(Duration.ofSeconds(10))
								.build()
				)
				.build());

		// Below - setting up circuit breaker with an id
		// Same id can be used for multiple services
		// Or separate ids can be created for each service's circuit breaker

//		factory.configure(configBuilder -> configBuilder
//			.circuitBreakerConfig(CircuitBreakerConfig.custom()
//					.failureRateThreshold(33)
//					.waitDurationInOpenState(Duration.ofSeconds(10))
//					.slidingWindow(10, 10, COUNT_BASED)
//					.build())
//				.build(), "downstream-service-id-or-name");
//
//		factory.configure(configBuilder -> configBuilder
//				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//				.timeLimiterConfig(
//						TimeLimiterConfig.custom()
//								.timeoutDuration(Duration.ofSeconds(10))
//								.build()
//				)
//				.build(), "downstream-service-id-or-name");

		return factory;
	}
}
