package com.example.consumer.service;

import com.example.provider.controller.api.UserAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@Service
// 最简单测试方法：Provider关闭（模拟连不上）
// @FeignClient(name="user-provider")
// 两种降级模式：
// @FeignClient(name="user-provider", fallbackFactory = UserProviderBackFactory.class)
@FeignClient(name="user-provider", fallback = UserProviderSimpleFallback.class)
public interface UserProviderFiegnClient extends UserAPI {

}
