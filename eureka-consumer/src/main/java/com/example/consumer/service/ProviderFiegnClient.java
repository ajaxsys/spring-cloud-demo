package com.example.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name="provider")
public interface ProviderFiegnClient {

    // 问题，还是硬编码，对着Provider提供的API来编码的, 和我们前端写API的URL一样
    @GetMapping("/hi")
    public String hi();

    @GetMapping("/getMap")
    public Map<String, Object> getMap(@RequestParam String name, @RequestParam int age);
}
