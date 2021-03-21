package com.example.consumer.controller;


import com.example.consumer.service.UserProviderFiegnClient;
import com.example.provider.controller.api.model.User;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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

}
