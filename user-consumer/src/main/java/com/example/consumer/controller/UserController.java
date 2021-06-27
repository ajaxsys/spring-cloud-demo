package com.example.consumer.controller;


import com.example.consumer.service.UserProviderFiegnClient;
import com.example.provider.controller.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Value("${server.port}")
    Integer port;

    @Autowired
    UserProviderFiegnClient fiegnClient;


    // http://192.168.0.138:7880/client11
    @GetMapping("/client11")
    public User callByFeignClientWithParameter() {
        return fiegnClient.findById(1);
    }

    // http://192.168.0.138:7880/error
    @GetMapping("/clientErr")
    public String testFallback() {
        return fiegnClient.fallback();
    }

}
