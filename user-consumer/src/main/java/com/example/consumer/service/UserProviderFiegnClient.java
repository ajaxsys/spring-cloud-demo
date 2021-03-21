package com.example.consumer.service;

import com.example.provider.controller.api.UserAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name="user-provider")
public interface UserProviderFiegnClient extends UserAPI {

}
