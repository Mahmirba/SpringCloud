package com.example.producer.controller;

import com.example.producer.model.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${server.port}")
    private Integer currentPort;

    @GetMapping(value = "/getAll", consumes = MediaType.APPLICATION_JSON_VALUE)

    public List<ProductDto> getProducts() throws Exception {
//        System.out.println("current port is -----------------------------------"+ currentPort);
//        if (currentPort == 8088) {
//            try {
//                Thread.sleep(20000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        Integer i = Double.valueOf(Math.random() * 100).intValue();
        System.out.println("Random number: "+ i);
//        if (i % 2 == 0)
//            throw new Exception();
        if (i % 2 == 0)
            Thread.sleep(1200);
        ProductDto dto = new ProductDto();
        List<ProductDto> productDtos = new ArrayList<>();
        dto.setName("Fereydoun");
        dto.setAge(36);
        dto.setId(1);
        productDtos.add(dto);
        dto = new ProductDto();
        dto.setName("Mahmir");
        dto.setAge(38);
        dto.setId(2);
        productDtos.add(dto);

        return  productDtos;
    }
}
