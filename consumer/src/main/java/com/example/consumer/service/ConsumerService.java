package com.example.consumer.service;

import com.example.consumer.model.ProductDto;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@AllArgsConstructor
public class ConsumerService {

    private final RestTemplate restTemplate;

    private final EurekaClient eurekaClient;

    public Integer getProducts(Integer par) {

        InstanceInfo instances = eurekaClient.getNextServerFromEureka("producer-client", false);

        String baseUrl = instances.getHomePageUrl();
        ProductDto dto = new ProductDto();
        List<ProductDto> productDtos = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        Map<String, Integer> params = new HashMap<>();
        params.put("p", par);
        ResponseEntity<List> exchange = restTemplate.exchange(baseUrl + "/product/getAll", HttpMethod.GET, httpEntity, List.class, params);
        for (Object p : exchange.getBody()) {
            LinkedHashMap lhm = (LinkedHashMap) p;
            ProductDto pDto = new ProductDto(String.valueOf(lhm.get("name")), (Integer) lhm.get("age"), (Integer) lhm.get("id"));
            productDtos.add(pDto);
        }
        Integer sum = productDtos.stream().reduce(0, (p, e2) -> p.intValue() + e2.getAge().intValue(), Integer::sum);
        int average = sum / productDtos.size();
        return average;
    }

    public Integer getProducts() {

        InstanceInfo instances = eurekaClient.getNextServerFromEureka("producer-client", false);

        String baseUrl = instances.getHomePageUrl();
        ProductDto dto = new ProductDto();
        List<ProductDto> productDtos = new ArrayList<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List> exchange = restTemplate.exchange(baseUrl + "/product/getAll", HttpMethod.GET, httpEntity, List.class);
        for (Object p : exchange.getBody()) {
            LinkedHashMap lhm = (LinkedHashMap) p;
            ProductDto pDto = new ProductDto(String.valueOf(lhm.get("name")), (Integer) lhm.get("age"), (Integer) lhm.get("id"));
            productDtos.add(pDto);
        }
        Integer sum = productDtos.stream().reduce(0, (p, e2) -> p.intValue() + e2.getAge().intValue(), Integer::sum);
        int average = sum / productDtos.size();
        return average;
    }
}
