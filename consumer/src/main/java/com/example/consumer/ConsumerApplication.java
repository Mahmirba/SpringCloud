package com.example.consumer;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
        ConsumerApplication consumerApplication = new ConsumerApplication();
        consumerApplication.displayDefaultValues();
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    void displayDefaultValues() {
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        System.out.println("failureRateThreshold = " + config.getFailureRateThreshold());
        System.out.println("minimumNumberOfCalls = " + config.getMinimumNumberOfCalls());
        System.out.println("permittedNumberOfCallsInHalfOpenState = " + config.getPermittedNumberOfCallsInHalfOpenState());
        System.out.println("maxWaitDurationInHalfOpenState = " + config.getMaxWaitDurationInHalfOpenState());
        System.out.println("slidingWindowSize = " + config.getSlidingWindowSize());
        System.out.println("slidingWindowType = " + config.getSlidingWindowType());
        System.out.println("slowCallRateThreshold = " + config.getSlowCallRateThreshold());
        System.out.println("slowCallDurationThreshold = " + config.getSlowCallDurationThreshold());
        System.out.println("automaticTransitionFromOpenToHalfOpenEnabled = " + config.isAutomaticTransitionFromOpenToHalfOpenEnabled());
        System.out.println("writableStackTraceEnabled = " + config.isWritableStackTraceEnabled());
    }
}
