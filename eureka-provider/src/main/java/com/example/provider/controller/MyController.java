package com.example.provider.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Value("${server.port}")
    Integer port;

    @GetMapping("/hi")
    String hi() {
        return "hi:" + port;
    }
}
