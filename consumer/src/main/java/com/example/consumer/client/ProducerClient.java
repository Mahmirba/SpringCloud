package com.example.consumer.client;

import com.example.consumer.config.LoadBalancerConfiguration;
import com.example.consumer.model.ProductDto;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "producer-client")
@LoadBalancerClient( name = "producer-client", configuration = LoadBalancerConfiguration.class)
public interface ProducerClient {

    @GetMapping(value = "/product/getAll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductDto> getProducts();
}
