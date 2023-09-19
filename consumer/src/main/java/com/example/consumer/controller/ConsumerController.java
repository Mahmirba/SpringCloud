package com.example.consumer.controller;

import com.example.consumer.service.ConsumerService;
import com.example.consumer.client.ProducerClient;
import com.example.consumer.model.ProductDto;
import com.netflix.discovery.EurekaClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@AllArgsConstructor
@RestController
@RequestMapping("/product")
public class ConsumerController {

    private final RestTemplate restTemplate;

    private final EurekaClient eurekaClient;

    private final ProducerClient producerClient;

    private final ConsumerService consumerService;

//    private final WebClient.Builder webClientBuilder;

    @GetMapping(value = "/get/summerize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer getProducts() {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(2.0f)
                .waitDurationInOpenState(Duration.ofSeconds(3))
//                .permittedNumberOfCallsInHalfOpenState(2)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .writableStackTraceEnabled(false)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("consumerService");
        Supplier<Integer> summerizes = () -> consumerService.getProducts();
//        Supplier<Integer> decoratedSummerizes = circuitBreaker.decorateSupplier(summerizes);
        Supplier<Integer> decoratedSummerizes = Decorators.ofSupplier(summerizes)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(Arrays.asList(Exception.class),
                        e -> this.getSummerizeFallBack())
                .decorate();

        for (int i=0; i<150; i++) {
            try {
                if (i== 50 || i== 70 || i== 125)
                    Thread.sleep(3000);
                System.out.println(decoratedSummerizes.get());
            }
            catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

//        return consumerService.getProducts();
        return 0;
    }

    @GetMapping(value = "/get/summerize/time/based", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer getProductsTestTimeBased() {
        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .minimumNumberOfCalls(5)
                .slidingWindowSize(10)
                .slowCallRateThreshold(61.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(1))
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
//                .permittedNumberOfCallsInHalfOpenState(2)
                .writableStackTraceEnabled(false)
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("consumerService");
        Supplier<Integer> summerizes = () -> consumerService.getProducts();
//        Supplier<Integer> decoratedSummerizes = circuitBreaker.decorateSupplier(summerizes);
        Supplier<Integer> decoratedSummerizes = Decorators.ofSupplier(summerizes)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(e -> this.getSummerizeFallBack())
                .decorate();

        for (int i=0; i<50; i++) {
            try {
                if (i== 50 || i== 70 || i== 125)
                    Thread.sleep(3000);
                System.out.println(decoratedSummerizes.get());
            }
            catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

//        return consumerService.getProducts();
        return 0;
    }

    private Integer getSummerizeFallBack(){
        try {
            Thread.sleep(900);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1000;
    }

    @GetMapping(value = "/load/balancer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer getProducts2() {

        List<ProductDto> productDtos = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
            productDtos = producerClient.getProducts();
//        }
        Integer sum = productDtos.stream().reduce(0, (p, e2) -> p.intValue() + e2.getAge().intValue(), Integer::sum);
        int average = sum / productDtos.size();

        return average;
    }

}
